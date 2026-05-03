package com.example.mambajet.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mambajet.domain.Trip
import com.example.mambajet.domain.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    /**
     * T5.2 — Canal de eventos de error para notificar a la UI cuando hay un nombre duplicado
     * u otro error de validación del repositorio.
     */
    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    fun setCurrentUser(userId: String) {
        Log.d(TAG, "Usuario activo cambiado a: $userId")
        _currentUserId.value = userId
    }

    fun addTrip(trip: Trip) {
        Log.d(TAG, "Intentando añadir viaje: ${trip.title}")
        if (!isValidTrip(trip)) {
            Log.e(TAG, "Validación fallida para: ${trip.title}")
            viewModelScope.launch { _errorEvent.emit("Datos del viaje inválidos. Revisa los campos.") }
            return
        }
        // Asigna el userId actual al viaje
        val tripWithUser = trip.copy(userId = _currentUserId.value)
        viewModelScope.launch {
            try {
                repository.addTrip(tripWithUser)
                Log.d(TAG, "Viaje guardado en DB: ${tripWithUser.title} para user: ${tripWithUser.userId}")
            } catch (e: IllegalArgumentException) {
                // T5.2 — Nombre duplicado detectado por el repositorio
                Log.w(TAG, "Error al añadir viaje: ${e.message}")
                _errorEvent.emit(e.message ?: "Error al guardar el viaje.")
            }
        }
    }

    fun editTrip(trip: Trip) {
        Log.d(TAG, "Intentando editar viaje: ${trip.title}")
        if (!isValidTrip(trip)) {
            Log.e(TAG, "Validación fallida al editar: ${trip.title}")
            viewModelScope.launch { _errorEvent.emit("Datos del viaje inválidos. Revisa los campos.") }
            return
        }
        viewModelScope.launch {
            try {
                repository.editTrip(trip)
                Log.d(TAG, "Viaje actualizado: ${trip.title}")
            } catch (e: IllegalArgumentException) {
                Log.w(TAG, "Error al editar viaje: ${e.message}")
                _errorEvent.emit(e.message ?: "Error al actualizar el viaje.")
            }
        }
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