package com.example.mambajet.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val title: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    val totalBudget: Double = 0.0,
    val spentBudget: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)