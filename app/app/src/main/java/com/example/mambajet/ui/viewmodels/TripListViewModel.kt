package com.example.mambajet.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mambajet.data.repository.TripRepositoryImpl
import com.example.mambajet.domain.Trip
import com.example.mambajet.domain.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TripListViewModel(private val repository: TripRepository = TripRepositoryImpl()) : ViewModel() {
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow()

    init {
        loadTrips()
    }

    fun loadTrips() {
        // Hacemos una copia de la lista (.toList()) para que Compose detecte el cambio de estado
        _trips.value = repository.getTrips().toList()
    }

    fun addTrip(trip: Trip) {
        repository.addTrip(trip)
        loadTrips()
    }

    fun editTrip(trip: Trip) {
        repository.editTrip(trip)
        loadTrips()
    }

    fun deleteTrip(tripId: String) {
        repository.deleteTrip(tripId)
        loadTrips()
    }
}