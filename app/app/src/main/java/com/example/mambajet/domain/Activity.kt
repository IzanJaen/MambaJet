package com.example.mambajet.domain

import java.util.Date


data class Activity(
    val id: String = java.util.UUID.randomUUID().toString(),
    val tripId: String, // Usaremos el destino (ej. "Tokio, Japón") para enlazarlo fácilmente
    var title: String,
    var description: String,
    var date: String,
    var time: String,
    var cost: Double,
    var type: PlanType
) {
    /**
     * Sincroniza la actividad con el calendario nativo del dispositivo (Google/Apple Calendar).
     */
    fun addToDeviceCalendar(): Boolean {
        // @TODO Implement integration with device's native calendar via Intent
        return false
    }

    /**
     * Devuelve la hora programada en formato legible para la UI.
     */
    fun getFormattedTime(): String {
        // @TODO Parse scheduledTime and return formatted time string (e.g., "14:30")
        return ""
    }

    /**
     * Comprueba si la actividad está a punto de ocurrir.
     */
    fun isUpcoming(): Boolean {
        // @TODO Check if the scheduled time is in the future compared to Date()
        return false
    }
}