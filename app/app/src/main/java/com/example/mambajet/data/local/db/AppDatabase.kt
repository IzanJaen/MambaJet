package com.example.mambajet.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mambajet.data.local.db.dao.ActivityDao
import com.example.mambajet.data.local.db.dao.TripDao
import com.example.mambajet.data.local.db.entity.ActivityEntity
import com.example.mambajet.data.local.db.entity.TripEntity

@Database(
    entities = [TripEntity::class, ActivityEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun activityDao(): ActivityDao
}