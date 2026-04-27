package com.example.mambajet.data.repository

import android.util.Log
import com.example.mambajet.data.local.db.dao.TripDao
import com.example.mambajet.data.local.db.mapper.toDomain
import com.example.mambajet.data.local.db.mapper.toEntity
import com.example.mambajet.domain.Trip
import com.example.mambajet.domain.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TripRepositoryImpl(private val tripDao: TripDao) : TripRepository {

    companion object {
        private const val TAG = "TripRepositoryImpl"
    }

    override fun getTripsFlow(): Flow<List<Trip>> {
        Log.d(TAG, "Obteniendo viajes desde Room DB")
        return tripDao.getAllTrips().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addTrip(trip: Trip) {
        Log.d(TAG, "Insertando viaje en Room DB: ${trip.title}")
        tripDao.insertTrip(trip.toEntity())
    }

    override suspend fun editTrip(trip: Trip) {
        Log.d(TAG, "Actualizando viaje en Room DB: ${trip.title}")
        tripDao.updateTrip(trip.toEntity())
    }

    override suspend fun deleteTrip(tripId: String) {
        Log.w(TAG, "Eliminando viaje de Room DB: $tripId")
        tripDao.deleteTripById(tripId)
    }

    override suspend fun updateTripBudget(tripId: String, spent: Double) {
        Log.d(TAG, "Actualizando presupuesto gastado para viaje: $tripId")
        tripDao.updateSpentBudget(tripId, spent)
    }
}