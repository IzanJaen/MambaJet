package com.example.mambajet.domain

interface ActivityRepository {
    fun getActivitiesForTrip(tripId: String): List<Activity>
    fun addActivity(activity: Activity)
    fun updateActivity(activity: Activity)
    fun deleteActivity(activityId: String)
}