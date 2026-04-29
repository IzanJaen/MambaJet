package com.example.mambajet.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * T4.1 — Entidad de usuario persistida en la base de datos local Room.
 * Almacena el perfil del usuario autenticado con Firebase.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,                         // Firebase UID
    val email: String,                      // login (email de Firebase)
    val username: String,                   // nombre de usuario único
    val birthdate: Long,                    // fecha de nacimiento (timestamp millis)
    val address: String = "",
    val country: String = "",
    val phone: String = "",
    val acceptEmails: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)