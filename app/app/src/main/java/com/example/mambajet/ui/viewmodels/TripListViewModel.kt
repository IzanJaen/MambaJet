package com.example.mambajet.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mambajet.domain.Trip
import com.example.mambajet.domain.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    companion object {
        private const val TAG = "TripListViewModel"
    }

    // El userId activo — se setea desde MainActivity al navegar a home
    private val _currentUserId = MutableStateFlow("")

    val trips: StateFlow<List<Trip>> = _currentUserId
        .flatMapLatest { uid ->
            if (uid.isBlank()) repository.getTripsFlow()
            else repository.getTripsByUser(uid)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setCurrentUser(userId: String) {
        Log.d(TAG, "Usuario activo cambiado a: $userId")
        _currentUserId.value = userId
    }

    fun addTrip(trip: Trip): Boolean {
        Log.d(TAG, "Intentando añadir viaje: ${trip.title}")
        if (!isValidTrip(trip)) {
            Log.e(TAG, "Validación fallida para: ${trip.title}")
            return false
        }
        // Asigna el userId actual al viaje
        val tripWithUser = trip.copy(userId = _currentUserId.value)
        viewModelScope.launch {
            repository.addTrip(tripWithUser)
            Log.d(TAG, "Viaje guardado en DB: ${tripWithUser.title} para user: ${tripWithUser.userId}")
        }
        return true
    }

    fun editTrip(trip: Trip): Boolean {
        Log.d(TAG, "Intentando editar viaje: ${trip.title}")
        if (!isValidTrip(trip)) {
            Log.e(TAG, "Validación fallida al editar: ${trip.title}")
            return false
        }
        viewModelScope.launch {
            repository.editTrip(trip)
        }
        return true
    }

    fun deleteTrip(tripId: String) {
        Log.w(TAG, "Eliminando viaje ID: $tripId")
        viewModelScope.launch {
            repository.deleteTrip(tripId)
        }
    }

    fun updateTripBudget(tripId: String, spent: Double) {
        viewModelScope.launch {
            repository.updateTripBudget(tripId, spent)
        }
    }

    private fun isValidTrip(trip: Trip): Boolean {
        if (trip.title.isBlank() || trip.startDate.isBlank() || trip.endDate.isBlank()) {
            Log.e(TAG, "Campos obligatorios vacíos")
            return false
        }
        val start = parseDate(trip.startDate)
        val end = parseDate(trip.endDate)
        if (start == null || end == null) {
            Log.e(TAG, "Formato de fecha incorrecto")
            return false
        }
        if (end.before(start)) {
            Log.e(TAG, "Fecha fin anterior a fecha inicio")
            return false
        }
        return true
    }

    private fun parseDate(dateStr: String): Date? {
        return try {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateStr)
        } catch (e: Exception) {
            null
        }
    }
}