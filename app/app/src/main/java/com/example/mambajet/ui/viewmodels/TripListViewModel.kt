package com.example.mambajet.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mambajet.data.repository.TripRepositoryImpl
import com.example.mambajet.domain.Trip
import com.example.mambajet.domain.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * [TripListViewModel] gestiona el estado de la UI para la lista de viajes.
 * Contiene la lógica de negocio y validación antes de interactuar con el Repositorio.
 */
class TripListViewModel(private val repository: TripRepository = TripRepositoryImpl()) : ViewModel() {

    companion object {
        private const val TAG = "TripListViewModel"
    }
    // Backing property: Evita que la UI modifique el estado directamente.
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow()

    init {
        Log.d(TAG, "ViewModel inicializado. Cargando viajes iniciales.")
        loadTrips()
    }

    /**
     * Carga los viajes desde el repositorio y actualiza el StateFlow.
     * Se utiliza .toList() para asegurar la inmutabilidad y forzar recomposición en Compose.
     */
    fun loadTrips() {
        _trips.value = repository.getTrips().toList()
        Log.d(TAG, "UI State actualizado con ${_trips.value.size} viajes.")
    }

    /**
     * Intenta añadir un nuevo viaje tras validar los datos.
     * @return true si la validación fue exitosa y se guardó, false si hubo error.
     */
    fun addTrip(trip: Trip): Boolean {
        Log.d(TAG, "Interacción de usuario: Intentando añadir viaje '${trip.title}'")
        if (!isValidTrip(trip)) {
            Log.e(TAG, "Validación fallida al añadir el viaje: ${trip.title}")
            return false
        }
        repository.addTrip(trip)
        loadTrips()
        return true
    }

    /**
     * Intenta editar un viaje tras validar los datos.
     */
    fun editTrip(trip: Trip): Boolean {
        Log.d(TAG, "Interacción de usuario: Intentando editar viaje '${trip.title}'")
        if (!isValidTrip(trip)) {
            Log.e(TAG, "Validación fallida al editar el viaje: ${trip.title}")
            return false
        }
        repository.editTrip(trip)
        loadTrips()
        return true
    }

    /**
     * Elimina un viaje dado su ID y refresca el estado.
     */
    fun deleteTrip(tripId: String) {
        Log.w(TAG, "Interacción de usuario: Solicitud de eliminación para el viaje ID: $tripId")
        repository.deleteTrip(tripId)
        loadTrips()
    }

    /**
     * Actualiza dinámicamente el presupuesto gastado (Usado desde el Itinerario).
     */
    fun updateTripBudget(tripId: String, spent: Double) {
        repository.updateTripBudget(tripId, spent)
        loadTrips()
    }

    /**
     * Valida la integridad de un objeto Trip (Reglas de negocio).
     * @param trip Objeto a validar.
     * @return true si es válido, false si contiene campos vacíos o fechas ilógicas.
     */
    private fun isValidTrip(trip: Trip): Boolean {
        if (trip.title.isBlank() || trip.startDate.isBlank() || trip.endDate.isBlank()) {
            Log.e(TAG, "Comportamiento inesperado: Campos obligatorios vacíos detectados.")
            return false
        }

        val start = parseDate(trip.startDate)
        val end = parseDate(trip.endDate)

        if (start == null || end == null) {
            Log.e(TAG, "Comportamiento inesperado: Formato de fecha incorrecto.")
            return false
        }
        if (end.before(start)) {
            Log.e(TAG, "Comportamiento inesperado: La fecha de fin (${trip.endDate}) es anterior al inicio (${trip.startDate}).")
            return false
        }

        return true
    }

    private fun parseDate(dateStr: String): Date? {
        return try {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateStr)
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al parsear la fecha: $dateStr", e)
            null
        }
    }
}