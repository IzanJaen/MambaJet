package com.example.mambajet.data.repository

import com.example.mambajet.data.local.db.dao.ActivityDao
import com.example.mambajet.data.local.db.mapper.toDomain
import com.example.mambajet.data.local.db.mapper.toEntity
import com.example.mambajet.domain.Activity
import com.example.mambajet.domain.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ActivityRepositoryImpl(private val activityDao: ActivityDao) : ActivityRepository {

    override fun getActivitiesForTripFlow(tripId: String): Flow<List<Activity>> {
        return activityDao.getActivitiesForTrip(tripId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addActivity(activity: Activity) {
        activityDao.insertActivity(activity.toEntity())
    }

    override suspend fun updateActivity(activity: Activity) {
        activityDao.updateActivity(activity.toEntity())
    }

    override suspend fun deleteActivity(activityId: String) {
        activityDao.deleteActivityById(activityId)
    }
}