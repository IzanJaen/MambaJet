package com.example.mambajet.data.repository

import com.example.mambajet.data.fakeDB.FakeTripDataSource
import com.example.mambajet.domain.Trip
import com.example.mambajet.domain.TripRepository

class TripRepositoryImpl : TripRepository {

    override fun getTrips(): List<Trip> {
        return FakeTripDataSource.getTrips()
    }

    override fun addTrip(trip: Trip) {
        FakeTripDataSource.addTrip(trip)
    }

    override fun editTrip(trip: Trip) {
        FakeTripDataSource.editTrip(trip)
    }

    override fun deleteTrip(tripId: String) {
        FakeTripDataSource.deleteTrip(tripId)
    }
}