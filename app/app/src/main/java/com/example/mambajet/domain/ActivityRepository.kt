package com.example.mambajet.domain

import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getActivitiesForTripFlow(tripId: String): Flow<List<Activity>>
    suspend fun addActivity(activity: Activity)
    suspend fun updateActivity(activity: Activity)
    suspend fun deleteActivity(activityId: String)
}