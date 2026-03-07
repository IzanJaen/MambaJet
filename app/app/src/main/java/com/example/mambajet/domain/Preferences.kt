package com.example.mambajet.domain

data class Preferences(
    val userId: String,
    val isDarkTheme: Boolean,
    val language: String,
    val pushNotifications: Boolean,
    val hapticFeedback: Boolean
) {
    /**
     * Guarda los ajustes modificados por el usuario.
     */
    fun updateSettings() {
        // @TODO Implement settings update logic (DataStore / SharedPreferences)
    }

    /**
     * Borra la memoria caché temporal de la app.
     */
    fun clearAppCache() {
        // @TODO Implement app cache clearing logic to free up space
    }


}
