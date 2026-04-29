package com.example.mambajet.domain

/**
 * T4.1 — Modelo de dominio del perfil de usuario almacenado localmente.
 */
data class UserProfile(
    val id: String,                 // Firebase UID
    val email: String,              // login
    val username: String,
    val birthdate: Long,            // timestamp millis
    val address: String = "",
    val country: String = "",
    val phone: String = "",
    val acceptEmails: Boolean = false
)