package com.example.mambajet.domain

import java.util.Date

data class Activity(
    val id: String,
    val tripId: String,
    val title: String,
    val type: PlanType,
    val scheduledTime: Date,
    val cost: Double,
    val location: String,
    val notes: String
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