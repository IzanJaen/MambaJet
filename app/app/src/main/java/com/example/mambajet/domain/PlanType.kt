package com.example.mambajet.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
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
        // @TODO Map and return corresponding Material icon for each PlanType enum
        return Icons.Default.Flight // Placeholder
    }
}