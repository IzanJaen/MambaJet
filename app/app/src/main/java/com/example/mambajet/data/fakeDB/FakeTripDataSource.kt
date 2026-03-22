package com.example.mambajet.data.fakeDB

import android.util.Log
import com.example.mambajet.domain.Trip

/**
 * FakeTripDataSource simula una base de datos local en memoria.
 * Al ser un 'object' (Singleton), mantiene el estado durante todo el ciclo de vida de la app.
 */
object FakeTripDataSource {
    private const val TAG = "FakeTripDataSource"
    private val trips = mutableListOf<Trip>()

    init {
        Log.d(TAG, "Inicializando la base de datos simulada con valores por defecto.")
        trips.add(Trip(title = "Tokio, Japón", startDate = "15/05/2026", endDate = "20/05/2026", description = "Viaje tecnológico", totalBudget = 2500.0, spentBudget = 1200.0))
        trips.add(Trip(title = "París, Francia", startDate = "02/06/2026", endDate = "05/06/2026", description = "Escapada europea", totalBudget = 1200.0, spentBudget = 450.0))
        trips.add(Trip(title = "Nueva York, USA", startDate = "10/12/2026", endDate = "15/12/2026", description = "Sueño americano", totalBudget = 3000.0, spentBudget = 150.0))
    }

    /**
     * Recupera la lista actual de viajes.
     * @return Lista inmutable de objetos [Trip].
     */
    fun getTrips(): List<Trip> {
        Log.d(TAG, "Obteniendo lista de viajes. Total actual: ${trips.size}")
        return trips
    }

    /**
     * Inserta un nuevo viaje en la memoria.
     */
    fun addTrip(trip: Trip) {
        trips.add(trip)
        Log.i(TAG, "Nuevo viaje añadido exitosamente: ${trip.title}")
    }

    /**
     * Actualiza un viaje existente buscando por ID o Título.
     */
    fun editTrip(updatedTrip: Trip) {
        val index = trips.indexOfFirst { it.id == updatedTrip.id || it.title == updatedTrip.title }
        if (index != -1) {
            trips[index] = updatedTrip
            Log.i(TAG, "Viaje actualizado correctamente: ${updatedTrip.title}")
        } else {
            Log.e(TAG, "Error al actualizar: No se encontró el viaje ${updatedTrip.title}")
        }
    }

    /**
     * Elimina un viaje de la memoria.
     */
    fun deleteTrip(tripIdOrTitle: String) {
        val removed = trips.removeAll { it.id == tripIdOrTitle || it.title == tripIdOrTitle }
        if (removed) {
            Log.i(TAG, "Viaje eliminado: $tripIdOrTitle")
        } else {
            Log.w(TAG, "Intento de eliminar un viaje que no existe: $tripIdOrTitle")
        }
    }

    /**
     * Actualiza dinámicamente el presupuesto gastado de un viaje.
     */
    fun updateTripBudget(tripIdOrTitle: String, spent: Double) {
        val index = trips.indexOfFirst { it.id == tripIdOrTitle || it.title == tripIdOrTitle }
        if (index != -1) {
            trips[index] = trips[index].copy(spentBudget = spent)
            Log.d(TAG, "Presupuesto actualizado para $tripIdOrTitle: $spent € gastados.")
        }
    }

    /**
     * Función exclusiva para entornos de Test.
     */
    fun clearForTesting() {
        trips.clear()
        Log.w(TAG, "¡ATENCIÓN! La base de datos ha sido purgada para pruebas (Testing).")
    }
}