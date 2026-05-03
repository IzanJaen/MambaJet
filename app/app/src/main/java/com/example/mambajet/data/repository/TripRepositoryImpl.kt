package com.example.mambajet.data.repository

import android.util.Log
import com.example.mambajet.data.local.db.dao.TripDao
import com.example.mambajet.data.local.db.mapper.toDomain
import com.example.mambajet.data.local.db.mapper.toEntity
import com.example.mambajet.domain.Trip
import com.example.mambajet.domain.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TripRepositoryImpl(private val tripDao: TripDao) : TripRepository {

    companion object {
        private const val TAG = "TripRepositoryImpl"
    }

    /** T4.2 — Solo los viajes del usuario logueado. */
    override fun getTripsByUser(userId: String): Flow<List<Trip>> {
        Log.d(TAG, "Obteniendo viajes de userId: $userId")
        return tripDao.getTripsByUser(userId).map { list -> list.map { it.toDomain() } }
    }

    override fun getTripsFlow(): Flow<List<Trip>> {
        Log.d(TAG, "Obteniendo todos los viajes desde Room DB")
        return tripDao.getAllTrips().map { list -> list.map { it.toDomain() } }
    }

    /**
     * T5.2 — Verifica si ya existe un viaje con el mismo título para el mismo usuario.
     * Lanza IllegalArgumentException si hay duplicado para que el ViewModel lo capture.
     */
    override suspend fun addTrip(trip: Trip) {
        Log.d(TAG, "Intentando insertar viaje: '${trip.title}' para userId: ${trip.userId}")

        val duplicate = checkDuplicateTripName(trip.title, trip.userId, excludeTripId = null)
        if (duplicate) {
            Log.e(TAG, "Nombre de viaje duplicado detectado: '${trip.title}'")
            throw IllegalArgumentException("Ya existe un viaje con el nombre '${trip.title}'")
        }

        Log.d(TAG, "Insertando viaje en Room DB: ${trip.title}")
        tripDao.insertTrip(trip.toEntity())
    }

    /**
     * T5.2 — Al editar también se comprueba que el nuevo título no colisione
     * con otro viaje existente del mismo usuario (excluyendo el propio).
     */
    override suspend fun editTrip(trip: Trip) {
        Log.d(TAG, "Actualizando viaje en Room DB: ${trip.title}")

        val duplicate = checkDuplicateTripName(trip.title, trip.userId, excludeTripId = trip.id)
        if (duplicate) {
            Log.e(TAG, "Nombre duplicado al editar: '${trip.title}'")
            throw IllegalArgumentException("Ya existe un viaje con el nombre '${trip.title}'")
        }

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

    /**
     * T5.2 — Comprueba si ya existe otro viaje del mismo usuario con el mismo título (case-insensitive).
     * @param excludeTripId Si no es null, excluye ese ID de la comprobación (útil al editar).
     */
    private suspend fun checkDuplicateTripName(
        title: String,
        userId: String,
        excludeTripId: String?
    ): Boolean {
        val userTrips = tripDao.getTripsByUser(userId).first()
        return userTrips.any { entity ->
            entity.title.trim().equals(title.trim(), ignoreCase = true) &&
                    entity.id != excludeTripId
        }
    }
}