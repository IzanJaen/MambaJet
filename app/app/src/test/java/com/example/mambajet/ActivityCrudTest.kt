package com.example.mambajet

import com.example.mambajet.data.local.db.dao.ActivityDao
import com.example.mambajet.data.local.db.entity.ActivityEntity
import com.example.mambajet.data.repository.ActivityRepositoryImpl
import com.example.mambajet.domain.Activity
import com.example.mambajet.domain.PlanType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * T5.1 — Unit tests del ActivityRepository usando un FakeActivityDao en memoria.
 * No requiere Android runtime: corre como JUnit puro en src/test/.
 */
class ActivityCrudTest {

    // ── Fake DAO en memoria ────────────────────────────────────────────────────

    private class FakeActivityDao : ActivityDao {

        private val activities = MutableStateFlow<List<ActivityEntity>>(emptyList())

        override fun getActivitiesForTrip(tripId: String): Flow<List<ActivityEntity>> =
            activities.map { list -> list.filter { it.tripId == tripId } }

        override suspend fun insertActivity(activity: ActivityEntity) {
            activities.update { current ->
                current.filterNot { it.id == activity.id } + activity
            }
        }

        override suspend fun updateActivity(activity: ActivityEntity) {
            activities.update { current ->
                current.map { if (it.id == activity.id) activity else it }
            }
        }

        override suspend fun deleteActivity(activity: ActivityEntity) {
            activities.update { it.filterNot { e -> e.id == activity.id } }
        }

        override suspend fun deleteActivityById(activityId: String) {
            activities.update { it.filterNot { e -> e.id == activityId } }
        }

        override suspend fun deleteActivitiesForTrip(tripId: String) {
            activities.update { it.filterNot { e -> e.tripId == tripId } }
        }
    }

    // ── Fixtures ──────────────────────────────────────────────────────────────

    private lateinit var repository: ActivityRepositoryImpl

    private fun makeActivity(
        id: String,
        tripId: String,
        title: String = "Actividad de prueba",
        cost: Double = 100.0
    ) = Activity(
        id = id,
        tripId = tripId,
        title = title,
        description = "Descripción",
        date = "01/01/2026",
        time = "10:00",
        cost = cost,
        type = PlanType.EXPLORATION
    )

    @Before
    fun setUp() {
        repository = ActivityRepositoryImpl(FakeActivityDao())
    }

    // ─── INSERT ───────────────────────────────────────────────────────────────

    @Test
    fun `addActivity should insert a new activity for a specific trip`() = runBlocking {
        val tripId = "trip_add"
        val activity = makeActivity(id = "act_add", tripId = tripId, title = "Vuelo a Roma")

        repository.addActivity(activity)

        var activities: List<Activity> = emptyList()
        repository.getActivitiesForTripFlow(tripId).collect { activities = it }

        val saved = activities.find { it.id == "act_add" }
        assertNotNull(saved)
        assertEquals("Vuelo a Roma", saved!!.title)
        assertEquals(tripId, saved.tripId)
    }

    @Test
    fun `addActivity should not appear in a different trip`() = runBlocking {
        repository.addActivity(makeActivity(id = "act_1", tripId = "trip_A"))
        repository.addActivity(makeActivity(id = "act_2", tripId = "trip_B"))

        var activitiesB: List<Activity> = emptyList()
        repository.getActivitiesForTripFlow("trip_B").collect { activitiesB = it }

        assertEquals(1, activitiesB.size)
        assertEquals("act_2", activitiesB.first().id)
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    @Test
    fun `updateActivity should modify existing activity details`() = runBlocking {
        val tripId = "trip_edit"
        val activity = makeActivity(id = "act_edit", tripId = tripId, title = "Vuelo a Roma", cost = 150.0)
        repository.addActivity(activity)

        val updated = activity.copy(title = "Vuelo Retrasado", cost = 200.0)
        repository.updateActivity(updated)

        var activities: List<Activity> = emptyList()
        repository.getActivitiesForTripFlow(tripId).collect { activities = it }

        val saved = activities.find { it.id == "act_edit" }
        assertNotNull(saved)
        assertEquals("Vuelo Retrasado", saved!!.title)
        assertEquals(200.0, saved.cost, 0.0)
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    @Test
    fun `deleteActivity should remove the activity from the itinerary`() = runBlocking {
        val tripId = "trip_delete"
        repository.addActivity(makeActivity(id = "act_delete", tripId = tripId, title = "Cena"))

        repository.deleteActivity("act_delete")

        var activities: List<Activity> = emptyList()
        repository.getActivitiesForTripFlow(tripId).collect { activities = it }

        assertNull(activities.find { it.id == "act_delete" })
        assertEquals(0, activities.size)
    }

    @Test
    fun `deleteActivity should not affect other activities in the same trip`() = runBlocking {
        val tripId = "trip_multi"
        repository.addActivity(makeActivity(id = "act_keep", tripId = tripId, title = "Museo"))
        repository.addActivity(makeActivity(id = "act_gone", tripId = tripId, title = "Tour"))

        repository.deleteActivity("act_gone")

        var activities: List<Activity> = emptyList()
        repository.getActivitiesForTripFlow(tripId).collect { activities = it }

        assertEquals(1, activities.size)
        assertEquals("Museo", activities.first().title)
    }
}