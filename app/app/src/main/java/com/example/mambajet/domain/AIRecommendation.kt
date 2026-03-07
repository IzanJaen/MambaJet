package com.example.mambajet.domain

import java.util.Date

data class AIRecommendation(
    val id: String,
    val tripId: String,
    val context: String,
    val suggestion: String,
    val generatedAt: Date
) {
    /**
     * Envía el contexto del viaje a la IA para recibir recomendaciones de itinerario.
     */
    fun generateAIRecommendatios() {
        // @TODO Implement AI prompt generation and backend API call (e.g., Gemini API)
    }

    /**
     * Analiza el presupuesto actual y sugiere recortes u optimizaciones.
     */
    fun suggestBudgetOptimizations() {
        // @TODO Implement logic to suggest budget alternatives via AI
    }

    /**
     * Convierte la recomendación de la IA directamente en una Activity real del viaje.
     */
    fun applyToTrip() {
        // @TODO Implement conversion of recommendation into a persistent Trip Activity
    }
}