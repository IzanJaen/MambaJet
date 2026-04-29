package com.example.mambajet.domain

import kotlinx.coroutines.flow.Flow

interface TripRepository {
    /** T4.2 — Devuelve solo los viajes del usuario indicado. */
    fun getTripsByUser(userId: String): Flow<List<Trip>>
    fun getTripsFlow(): Flow<List<Trip>>  // mantener para compatibilidad
    suspend fun addTrip(trip: Trip)
    suspend fun editTrip(trip: Trip)
    suspend fun deleteTrip(tripId: String)
    suspend fun updateTripBudget(tripId: String, spent: Double)
}