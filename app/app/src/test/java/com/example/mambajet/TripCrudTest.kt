package com.example.mambajet

import com.example.mambajet.data.local.db.dao.TripDao
import com.example.mambajet.data.local.db.entity.TripEntity
import com.example.mambajet.data.repository.TripRepositoryImpl
import com.example.mambajet.domain.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * T5.1 — Unit tests del TripRepository usando un FakeTripDao en memoria.
 * No requiere Android runtime: corre como JUnit puro en src/test/.
 */
class TripCrudTest {

    // ── Fake DAO en memoria ────────────────────────────────────────────────────

    private class FakeTripDao : TripDao {

        private val trips = MutableStateFlow<List<TripEntity>>(emptyList())

        override fun getTripsByUser(userId: String): Flow<List<TripEntity>> =
            trips.map { list -> list.filter { it.userId == userId } }

        override fun getAllTrips(): Flow<List<TripEntity>> = trips

        override suspend fun getTripById(tripId: String): TripEntity? =
            trips.value.find { it.id == tripId }

        override suspend fun countByTitleAndUser(
            title: String,
            userId: String,
            excludeId: String?
        ): Int = trips.value.count { entity ->
            entity.userId == userId &&
                    entity.title.trim().equals(title.trim(), ignoreCase = true) &&
                    entity.id != excludeId
        }

        override suspend fun insertTrip(trip: TripEntity) {
            trips.update { current ->
                current.filterNot { it.id == trip.id } + trip
            }
        }

        override suspend fun updateTrip(trip: TripEntity) {
            trips.update { current ->
                current.map { if (it.id == trip.id) trip else it }
            }
        }

        override suspend fun deleteTrip(trip: TripEntity) {
            trips.update { it.filterNot { e -> e.id == trip.id } }
        }

        override suspend fun deleteTripById(tripId: String) {
            trips.update { it.filterNot { e -> e.id == tripId } }
        }

        override suspend fun updateSpentBudget(tripId: String, spent: Double) {
            trips.update { current ->
                current.map { if (it.id == tripId) it.copy(spentBudget = spent) else it }
            }
        }
    }

    // ── Fixtures ──────────────────────────────────────────────────────────────

    private lateinit var repository: TripRepositoryImpl
    private val testUserId = "user_test"

    private fun makeTrip(id: String = "trip_1", title: String = "Roma") = Trip(
        id = id,
        userId = testUserId,
        title = title,
        startDate = "01/01/2026",
        endDate = "05/01/2026",
        description = "Test",
        totalBudget = 1000.0,
        spentBudget = 0.0
    )

    @Before
    fun setUp() {
        repository = TripRepositoryImpl(FakeTripDao())
    }

    // ─── INSERT ───────────────────────────────────────────────────────────────

    @Test
    fun `addTrip should insert a new trip into the repository`() = runBlocking {
        repository.addTrip(makeTrip())

        var trips: List<Trip> = emptyList()
        repository.getTripsByUser(testUserId).collect { trips = it }

        assertEquals(1, trips.size)
        assertEquals("Roma", trips.first().title)
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    @Test
    fun `editTrip should update existing trip details`() = runBlocking {
        val trip = makeTrip()
        repository.addTrip(trip)

        val updatedTrip = trip.copy(title = "Roma Actualizada", totalBudget = 1500.0)
        repository.editTrip(updatedTrip)

        var trips: List<Trip> = emptyList()
        repository.getTripsByUser(testUserId).collect { trips = it }

        assertEquals("Roma Actualizada", trips.first().title)
        assertEquals(1500.0, trips.first().totalBudget, 0.0)
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    @Test
    fun `deleteTrip should remove the trip from repository`() = runBlocking {
        repository.addTrip(makeTrip())
        repository.deleteTrip("trip_1")

        var trips: List<Trip> = emptyList()
        repository.getTripsByUser(testUserId).collect { trips = it }

        assertTrue(trips.isEmpty())
    }

    // ─── DUPLICATE VALIDATION (T5.2) ──────────────────────────────────────────

    @Test(expected = IllegalArgumentException::class)
    fun `addTrip with duplicate name should throw IllegalArgumentException`() = runBlocking {
        repository.addTrip(makeTrip(id = "trip_1", title = "Roma"))
        repository.addTrip(makeTrip(id = "trip_2", title = "Roma")) // debe lanzar
    }

    @Test(expected = IllegalArgumentException::class)
    fun `addTrip with duplicate name case insensitive should throw`() = runBlocking {
        repository.addTrip(makeTrip(id = "trip_1", title = "Roma"))
        repository.addTrip(makeTrip(id = "trip_2", title = "ROMA"))
    }

    @Test
    fun `editTrip keeping same name should not throw`() = runBlocking {
        val trip = makeTrip(id = "trip_1", title = "Roma")
        repository.addTrip(trip)
        // Editar el mismo viaje con el mismo título no debe lanzar excepción
        repository.editTrip(trip.copy(totalBudget = 2000.0))
    }
}