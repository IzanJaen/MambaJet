    package com.example.mambajet.domain

import java.util.Date

data class Trip(
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String = "mamba_elite_user",
    var title: String,          // Requerido: En lugar de destination
    var startDate: String,      // Requerido: Formato dd/MM/YYYY
    var endDate: String,        // Requerido: Formato dd/MM/YYYY
    var description: String,    // Requerido: Nueva variable
    var totalBudget: Double = 0.0,
    var spentBudget: Double = 0.0
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