package com.example.mambajet.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class PlanType {
    FLIGHT,
    HOTEL,
    RESTAURANT,
    TRANSPORT,
    EXPLORATION;

    /**
     * Obtiene el icono visual asociado al tipo de plan.
     */
    fun getIcon(): ImageVector {
        return when(this) {
            FLIGHT -> Icons.Default.Flight
            HOTEL -> Icons.Default.Hotel
            RESTAURANT -> Icons.Default.Restaurant
            TRANSPORT -> Icons.Default.DirectionsCar
            EXPLORATION -> Icons.Default.Explore
        }
    }
}