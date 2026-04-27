package com.example.mambajet.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mambajet_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}