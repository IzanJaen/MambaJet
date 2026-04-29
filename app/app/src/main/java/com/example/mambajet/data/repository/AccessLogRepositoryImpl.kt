package com.example.mambajet.data.repository

import android.util.Log
import com.example.mambajet.data.local.db.dao.AccessLogDao
import com.example.mambajet.data.local.db.entity.AccessLogEntity
import com.example.mambajet.data.local.db.mapper.toDomain
import com.example.mambajet.domain.AccessLog
import com.example.mambajet.domain.AccessLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * T4.4 — Implementación del repositorio de logs de acceso.
 */
class AccessLogRepositoryImpl @Inject constructor(
    private val accessLogDao: AccessLogDao
) : AccessLogRepository {

    companion object {
        private const val TAG = "AccessLogRepository"
    }

    override suspend fun logLogin(userId: String) {
        Log.d(TAG, "LOGIN registrado para userId: $userId")
        accessLogDao.insertLog(
            AccessLogEntity(
                userId = userId,
                event = "LOGIN",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun logLogout(userId: String) {
        Log.d(TAG, "LOGOUT registrado para userId: $userId")
        accessLogDao.insertLog(
            AccessLogEntity(
                userId = userId,
                event = "LOGOUT",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override fun getLogsForUser(userId: String): Flow<List<AccessLog>> {
        return accessLogDao.getLogsForUser(userId).map { list -> list.map { it.toDomain() } }
    }
}