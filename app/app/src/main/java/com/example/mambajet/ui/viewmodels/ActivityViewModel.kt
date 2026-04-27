package com.example.mambajet.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mambajet.data.local.db.AppDatabase
import com.example.mambajet.data.repository.ActivityRepositoryImpl
import com.example.mambajet.domain.Activity
import com.example.mambajet.domain.ActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository: ActivityRepository = ActivityRepositoryImpl(db.activityDao())

    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities.asStateFlow()

    fun loadActivities(tripId: String) {
        viewModelScope.launch {
            repository.getActivitiesForTripFlow(tripId).collect { list ->
                _activities.value = list
            }
        }
    }

    fun addActivity(activity: Activity, tripStart: String, tripEnd: String): Boolean {
        if (!isValidActivity(activity, tripStart, tripEnd)) return false
        viewModelScope.launch {
            repository.addActivity(activity)
        }
        return true
    }

    fun updateActivity(activity: Activity, tripStart: String, tripEnd: String): Boolean {
        if (!isValidActivity(activity, tripStart, tripEnd)) return false
        viewModelScope.launch {
            repository.updateActivity(activity)
        }
        return true
    }

    fun deleteActivity(activityId: String, tripId: String) {
        viewModelScope.launch {
            repository.deleteActivity(activityId)
        }
    }

    private fun isValidActivity(activity: Activity, tripStart: String, tripEnd: String): Boolean {
        if (activity.title.isBlank() || activity.date.isBlank() || activity.time.isBlank()) return false
        val actDate = parseDate(activity.date)
        val tStart = parseDate(tripStart)
        val tEnd = parseDate(tripEnd)
        if (actDate == null || tStart == null || tEnd == null) return false
        if (actDate.before(tStart) || actDate.after(tEnd)) return false
        return true
    }

    private fun parseDate(dateStr: String): Date? {
        return try { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateStr) } catch (e: Exception) { null }
    }
}