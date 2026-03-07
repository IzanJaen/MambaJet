package com.example.mambajet.domain

import java.util.Date

data class Trip(
    val id: String,
    val userId: String,
    val destination: String,
    val startDate: Date,
    val endDate: Date,
    val totalBudget: Double,
    val spentBudget: Double
) {
    fun createTrip() {
        // @TODO Implement trip creation logic and save to DB
    }

    fun deleteTrip() {
        // @TODO Implement trip deletion logic including related activities and images
    }

    fun modifyTrip() {
        // @TODO Implement trip modification logic
    }

    /**
     * Calcula el presupuesto restante restando los gastos al presupuesto total.
     */
    fun calculateRemainingBudget(): Double {
        // @TODO Calculate and return remaining budget
        return totalBudget - spentBudget
    }

    /**
     * Calcula la duración total del viaje en días.
     */
    fun getDurationInDays(): Int {
        // @TODO Calculate trip duration in days based on startDate and endDate
        return 0
    }

    /**
     * Calcula el límite de gasto diario recomendado basado en el presupuesto restante y los días que faltan.
     */
    fun calculateDailyBudgetAllowance(): Double {
        // @TODO Implement smart logic to calculate safe daily allowance
        return 0.0
    }
}