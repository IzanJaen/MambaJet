package com.example.mambajet.data.local.db.dao

import androidx.room.*
import com.example.mambajet.data.local.db.entity.AccessLogEntity
import kotlinx.coroutines.flow.Flow

/**
 * T4.4 — DAO para persistir cada evento de login / logout.
 */
@Dao
interface AccessLogDao {

    /** Inserta un nuevo registro de acceso (login o logout). */
    @Insert
    suspend fun insertLog(log: AccessLogEntity)

    /** Devuelve todos los registros de un usuario ordenados por fecha descendente. */
    @Query("SELECT * FROM access_log WHERE userId = :userId ORDER BY timestamp DESC")
    fun getLogsForUser(userId: String): Flow<List<AccessLogEntity>>

    /** Todos los registros (útil para debug / admin). */
    @Query("SELECT * FROM access_log ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<AccessLogEntity>>
}