package com.example.mambajet.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey
    val id: String,
    val tripId: String,
    val title: String,
    val description: String,
    val date: String,           // texto: "dd/MM/yyyy"
    val time: String,
    val cost: Double,
    val type: String,           // guardaremos el nombre del enum como String
    val createdAt: Long = System.currentTimeMillis()  // datetime
)