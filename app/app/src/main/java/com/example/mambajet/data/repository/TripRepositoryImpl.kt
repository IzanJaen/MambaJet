package com.example.mambajet.data.repository

import android.util.Log
import com.example.mambajet.data.fakeDB.FakeTripDataSource
import com.example.mambajet.domain.Trip
import com.example.mambajet.domain.TripRepository

/**
 * Implementación de [TripRepository].
 * Actúa como única fuente de la verdad (Single Source of Truth) separando
 * la capa de dominio de la capa de acceso a datos.
 */
class TripRepositoryImpl : TripRepository {

    companion object {
        private const val TAG = "TripRepositoryImpl"
    }

    override fun getTrips(): List<Trip> {
        Log.d(TAG, "Delegando petición getTrips() al DataSource.")
        return FakeTripDataSource.getTrips()
    }

    override fun addTrip(trip: Trip) {
        Log.d(TAG, "Delegando inserción de viaje al DataSource: ${trip.title}")
        FakeTripDataSource.addTrip(trip)
    }

    override fun editTrip(trip: Trip) {
        FakeTripDataSource.editTrip(trip)
    }

    override fun deleteTrip(tripId: String) {
        FakeTripDataSource.deleteTrip(tripId)
    }

    override fun updateTripBudget(tripId: String, spent: Double) {
        FakeTripDataSource.updateTripBudget(tripId, spent)
    }
}