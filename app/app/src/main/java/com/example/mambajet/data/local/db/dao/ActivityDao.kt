package com.example.mambajet.data.local.db.dao

import androidx.room.*
import com.example.mambajet.data.local.db.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities WHERE tripId = :tripId ORDER BY date ASC, time ASC")
    fun getActivitiesForTrip(tripId: String): Flow<List<ActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)

    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    @Delete
    suspend fun deleteActivity(activity: ActivityEntity)

    @Query("DELETE FROM activities WHERE id = :activityId")
    suspend fun deleteActivityById(activityId: String)

    @Query("DELETE FROM activities WHERE tripId = :tripId")
    suspend fun deleteActivitiesForTrip(tripId: String)
}