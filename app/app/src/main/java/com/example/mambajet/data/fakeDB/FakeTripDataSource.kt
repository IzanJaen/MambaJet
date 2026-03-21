package com.example.mambajet.data.fakeDB

import com.example.mambajet.domain.Trip

object FakeTripDataSource {
    private val trips = mutableListOf<Trip>()

    // Cargamos un dataset falso (opcional) para que tu app no se vea vacía al iniciar
    init {
        trips.add(Trip(title = "Tokio, Japón", startDate = "15/05/2026", endDate = "20/05/2026", description = "Viaje tecnológico", totalBudget = 2500.0, spentBudget = 1200.0))
        trips.add(Trip(title = "París, Francia", startDate = "02/06/2026", endDate = "05/06/2026", description = "Escapada europea", totalBudget = 1200.0, spentBudget = 450.0))
    }

    fun getTrips(): List<Trip> {
        return trips
    }

    fun addTrip(trip: Trip) {
        trips.add(trip)
    }

    fun editTrip(updatedTrip: Trip) {
        val index = trips.indexOfFirst { it.id == updatedTrip.id }
        if (index != -1) {
            trips[index] = updatedTrip
        }
    }

    fun deleteTrip(tripId: String) {
        trips.removeAll { it.id == tripId }
    }
}