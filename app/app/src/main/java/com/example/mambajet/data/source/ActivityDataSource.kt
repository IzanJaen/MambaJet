package com.example.mambajet.data.source

import com.example.mambajet.domain.Activity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.UUID

class ActivityDataSource {
    // Simulamos una base de datos en memoria
    private val _activities = MutableStateFlow<List<Activity>>(emptyList())

    fun getActivitiesByTrip(tripId: String): Flow<List<Activity>> {
        return _activities.map { list -> list.filter { it.tripId == tripId } }
    }

    fun addActivity(activity: Activity) {
        val newActivity = if (activity.id.isEmpty()) activity.copy(id = UUID.randomUUID().toString()) else activity
        _activities.value = _activities.value + newActivity
    }

    fun updateActivity(activity: Activity) {
        _activities.value = _activities.value.map { if (it.id == activity.id) activity else it }
    }

    fun deleteActivity(activityId: String) {
        _activities.value = _activities.value.filter { it.id != activityId }
    }
}