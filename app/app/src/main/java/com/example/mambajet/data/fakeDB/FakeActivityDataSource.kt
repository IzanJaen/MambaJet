package com.example.mambajet.data.fakeDB

import com.example.mambajet.domain.Activity
import com.example.mambajet.domain.PlanType

object FakeActivityDataSource {
    private val activities = mutableListOf<Activity>()

    init {
        // Actividades por defecto para Tokio
        activities.add(Activity(tripId = "Tokio, Japón", title = "Vuelo MJ-200", description = "Iberia - Terminal 3", date = "15/05/2026", time = "10:00", cost = 450.0, type = PlanType.FLIGHT))
        activities.add(Activity(tripId = "Tokio, Japón", title = "Hotel Imperial", description = "Check in: Shinjuku", date = "15/05/2026", time = "15:00", cost = 850.0, type = PlanType.HOTEL))
        activities.add(Activity(tripId = "Tokio, Japón", title = "Mamba City Tour", description = "Exploración de Shibuya", date = "16/05/2026", time = "11:00", cost = 45.0, type = PlanType.EXPLORATION))

        // Actividades por defecto para París
        activities.add(Activity(tripId = "París, Francia", title = "Museo del Louvre", description = "Entrada VIP", date = "02/06/2026", time = "09:30", cost = 35.0, type = PlanType.EXPLORATION))
        activities.add(Activity(tripId = "París, Francia", title = "Cena Torre Eiffel", description = "Restaurante Le Jules Verne", date = "03/06/2026", time = "21:00", cost = 150.0, type = PlanType.RESTAURANT))

        // Actividades por defecto para Nueva York
        activities.add(Activity(tripId = "Nueva York, USA", title = "Helicóptero Manhattan", description = "Vuelo de 30 min", date = "11/12/2026", time = "16:00", cost = 200.0, type = PlanType.EXPLORATION))
    }

    fun getActivitiesForTrip(tripId: String): List<Activity> {
        return activities.filter { it.tripId == tripId }.sortedBy { it.time }
    }

    fun addActivity(activity: Activity) { activities.add(activity) }

    fun updateActivity(updatedActivity: Activity) {
        val index = activities.indexOfFirst { it.id == updatedActivity.id }
        if (index != -1) activities[index] = updatedActivity
    }

    fun deleteActivity(activityId: String) {
        activities.removeAll { it.id == activityId }
    }
    fun clearForTesting() {
        activities.clear()
    }
}
