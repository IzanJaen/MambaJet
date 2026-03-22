package com.example.mambajet.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mambajet.data.repository.ActivityRepositoryImpl
import com.example.mambajet.domain.Activity
import com.example.mambajet.domain.ActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActivityViewModel(private val repository: ActivityRepository = ActivityRepositoryImpl()) : ViewModel() {

    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities.asStateFlow()

    fun loadActivities(tripId: String) {
        val list = repository.getActivitiesForTrip(tripId)
        _activities.value = list.sortedWith(compareBy({ it.date }, { it.time }))
    }

    // --- T3.1: VALIDACIÓN EN LA CAPA VIEWMODEL ---
    fun addActivity(activity: Activity, tripStart: String, tripEnd: String): Boolean {
        if (!isValidActivity(activity, tripStart, tripEnd)) return false
        repository.addActivity(activity)
        loadActivities(activity.tripId)
        return true
    }

    fun updateActivity(activity: Activity, tripStart: String, tripEnd: String): Boolean {
        if (!isValidActivity(activity, tripStart, tripEnd)) return false
        repository.updateActivity(activity)
        loadActivities(activity.tripId)
        return true
    }

    // Comprueba campos vacíos y que la actividad esté dentro del rango del viaje
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
    // ---------------------------------------------

    fun deleteActivity(activityId: String, tripId: String) {
        repository.deleteActivity(activityId)
        loadActivities(tripId)
    }
}