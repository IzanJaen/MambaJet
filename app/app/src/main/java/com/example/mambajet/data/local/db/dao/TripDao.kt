package com.example.mambajet.data.local.db.dao

import androidx.room.*
import com.example.mambajet.data.local.db.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {

    /** T4.2 — Devuelve solo los viajes del usuario logueado. */
    @Query("SELECT * FROM trips WHERE userId = :userId ORDER BY createdAt DESC")
    fun getTripsByUser(userId: String): Flow<List<TripEntity>>

    /** Mantener para compatibilidad / admin. */
    @Query("SELECT * FROM trips ORDER BY createdAt DESC")
    fun getAllTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :tripId LIMIT 1")
    suspend fun getTripById(tripId: String): TripEntity?

    /**
     * T5.2 — Cuenta viajes del mismo usuario con el mismo título (case-insensitive),
     * opcionalmente excluyendo un ID (para el caso de edición).
     */
    @Query(
        "SELECT COUNT(*) FROM trips " +
                "WHERE userId = :userId " +
                "AND LOWER(TRIM(title)) = LOWER(TRIM(:title)) " +
                "AND (:excludeId IS NULL OR id != :excludeId)"
    )
    suspend fun countByTitleAndUser(title: String, userId: String, excludeId: String?): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity)

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Delete
    suspend fun deleteTrip(trip: TripEntity)

    @Query("DELETE FROM trips WHERE id = :tripId")
    suspend fun deleteTripById(tripId: String)

    @Query("UPDATE trips SET spentBudget = :spent WHERE id = :tripId")
    suspend fun updateSpentBudget(tripId: String, spent: Double)
}