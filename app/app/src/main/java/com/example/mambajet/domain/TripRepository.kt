package com.example.mambajet.domain

import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getTripsFlow(): Flow<List<Trip>>
    suspend fun addTrip(trip: Trip)
    suspend fun editTrip(trip: Trip)
    suspend fun deleteTrip(tripId: String)
    suspend fun updateTripBudget(tripId: String, spent: Double)
}