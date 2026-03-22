package com.example.mambajet.domain

interface TripRepository {
    fun getTrips(): List<Trip>
    fun addTrip(trip: Trip)
    fun editTrip(trip: Trip)
    fun deleteTrip(tripId: String)
    fun updateTripBudget(tripId: String, spent: Double)
}