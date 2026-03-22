package com.example.mambajet.data.repository

import com.example.mambajet.data.fakeDB.FakeActivityDataSource
import com.example.mambajet.domain.Activity
import com.example.mambajet.domain.ActivityRepository

class ActivityRepositoryImpl : ActivityRepository {

    override fun getActivitiesForTrip(tripId: String): List<Activity> {
        return FakeActivityDataSource.getActivitiesForTrip(tripId)
    }

    override fun addActivity(activity: Activity) {
        FakeActivityDataSource.addActivity(activity)
    }

    override fun updateActivity(activity: Activity) {
        FakeActivityDataSource.updateActivity(activity)
    }

    override fun deleteActivity(activityId: String) {
        FakeActivityDataSource.deleteActivity(activityId)
    }
}