package com.example.mambajet.domain

import kotlinx.coroutines.flow.Flow

/**
 * T4.4 — Contrato del repositorio de logs de acceso.
 */
interface AccessLogRepository {
    suspend fun logLogin(userId: String)
    suspend fun logLogout(userId: String)
    fun getLogsForUser(userId: String): Flow<List<AccessLog>>
}

data class AccessLog(
    val id: Int = 0,
    val userId: String,
    val event: String,      // "LOGIN" o "LOGOUT"
    val timestamp: Long
)