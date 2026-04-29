package com.example.mambajet.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * T4.4 — Registro de cada evento de login / logout.
 * Persiste el userId y el datetime exacto del evento.
 */
@Entity(tableName = "access_log")
data class AccessLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val event: String,              // "LOGIN" o "LOGOUT"
    val timestamp: Long = System.currentTimeMillis()
)