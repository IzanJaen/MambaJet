package com.example.mambajet

import com.example.mambajet.data.repository.TripRepositoryImpl
import com.example.mambajet.domain.Trip
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TripCrudTest {

    private lateinit var repository: TripRepositoryImpl

    @Before
    fun setUp() {
        repository = TripRepositoryImpl()

        // ¡LA SOLUCIÓN! Añadimos .toList() para hacer una copia de la lista antes de borrar
        // Así evitamos el ConcurrentModificationException
        repository.getTrips().toList().forEach { repository.deleteTrip(it.id) }
    }

    @Test
    fun `addTrip should insert a new trip into the repository`() {
        val newTrip = Trip(
            id = "test_trip_1",
            title = "Roma",
            startDate = "01/01/2026",
            endDate = "05/01/2026",
            description = "Test",
            totalBudget = 1000.0,
            spentBudget = 0.0
        )

        repository.addTrip(newTrip)
        val trips: List<Trip> = repository.getTrips()

        assertEquals(1, trips.size)
        assertEquals("Roma", trips.first().title)
    }

    @Test
    fun `editTrip should update existing trip details`() {
        val trip = Trip(id = "test_trip_1", title = "Roma", startDate = "01/01/2026", endDate = "05/01/2026", description = "Test", totalBudget = 1000.0, spentBudget = 0.0)
        repository.addTrip(trip)

        val updatedTrip = trip.copy(title = "Roma Actualizada", totalBudget = 1500.0)
        repository.editTrip(updatedTrip)

        val trips: List<Trip> = repository.getTrips()

        assertEquals("Roma Actualizada", trips.first().title)
        assertEquals(1500.0, trips.first().totalBudget, 0.0)
    }

    @Test
    fun `deleteTrip should remove the trip from repository`() {
        val trip = Trip(id = "test_trip_1", title = "Roma", startDate = "01/01/2026", endDate = "05/01/2026", description = "Test", totalBudget = 1000.0, spentBudget = 0.0)
        repository.addTrip(trip)

        repository.deleteTrip("test_trip_1")
        val trips: List<Trip> = repository.getTrips()

        assertEquals(0, trips.size)
    }
}