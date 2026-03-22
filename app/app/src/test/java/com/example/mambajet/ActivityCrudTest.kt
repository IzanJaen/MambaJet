package com.example.mambajet

import com.example.mambajet.data.repository.ActivityRepositoryImpl
import com.example.mambajet.domain.Activity
import com.example.mambajet.domain.PlanType
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ActivityCrudTest {

    private lateinit var repository: ActivityRepositoryImpl

    @Before
    fun setUp() {
        repository = ActivityRepositoryImpl()
    }

    @Test
    fun `addActivity should insert a new activity for a specific trip`() {
        // Usamos IDs exclusivos para esta prueba
        val tripId = "trip_add"
        val actId = "act_add"

        val activity = Activity(id = actId, tripId = tripId, title = "Vuelo a Roma", description = "Iberia", date = "01/01/2026", time = "10:00", cost = 150.0, type = PlanType.EXPLORATION)

        repository.addActivity(activity)
        val activities = repository.getActivitiesForTrip(tripId)
        val savedActivity = activities.find { it.id == actId }

        assertEquals(true, savedActivity != null)
        assertEquals("Vuelo a Roma", savedActivity?.title)
        assertEquals(tripId, savedActivity?.tripId)
    }

    @Test
    fun `updateActivity should modify existing activity details`() {
        // Usamos IDs exclusivos para esta prueba
        val tripId = "trip_edit"
        val actId = "act_edit"

        val activity = Activity(id = actId, tripId = tripId, title = "Vuelo a Roma", description = "Iberia", date = "01/01/2026", time = "10:00", cost = 150.0, type = PlanType.EXPLORATION)
        repository.addActivity(activity)

        val updatedActivity = activity.copy(title = "Vuelo Retrasado", cost = 200.0)
        repository.updateActivity(updatedActivity)

        val activities = repository.getActivitiesForTrip(tripId)
        val savedActivity = activities.find { it.id == actId }

        assertEquals("Vuelo Retrasado", savedActivity?.title)
        assertEquals(200.0, savedActivity?.cost ?: 0.0, 0.0)
    }

    @Test
    fun `deleteActivity should remove the activity from the itinerary`() {
        // Usamos IDs exclusivos para esta prueba
        val tripId = "trip_delete"
        val actId = "act_delete"

        val activity = Activity(id = actId, tripId = tripId, title = "Cena", description = "Pasta", date = "01/01/2026", time = "21:00", cost = 30.0, type = PlanType.EXPLORATION)
        repository.addActivity(activity)

        val initialSize = repository.getActivitiesForTrip(tripId).size

        repository.deleteActivity(actId)
        val finalSize = repository.getActivitiesForTrip(tripId).size

        assertEquals(initialSize - 1, finalSize)
    }
}