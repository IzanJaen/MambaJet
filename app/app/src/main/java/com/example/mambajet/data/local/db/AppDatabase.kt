package com.example.mambajet.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mambajet.data.local.db.dao.AccessLogDao
import com.example.mambajet.data.local.db.dao.ActivityDao
import com.example.mambajet.data.local.db.dao.TripDao
import com.example.mambajet.data.local.db.dao.UserDao
import com.example.mambajet.data.local.db.entity.AccessLogEntity
import com.example.mambajet.data.local.db.entity.ActivityEntity
import com.example.mambajet.data.local.db.entity.TripEntity
import com.example.mambajet.data.local.db.entity.UserEntity

/**
 * T4.1 / T4.4 — Base de datos Room actualizada a versión 2.
 * Añade las tablas: users y access_log.
 * La tabla trips ya tenía la columna userId desde Sprint 02.
 */
@Database(
    entities = [
        TripEntity::class,
        ActivityEntity::class,
        UserEntity::class,      // T4.1
        AccessLogEntity::class  // T4.4
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun activityDao(): ActivityDao
    abstract fun userDao(): UserDao          // T4.1
    abstract fun accessLogDao(): AccessLogDao // T4.4

    companion object {
        /**
         * Migración 1 → 2: Crea las tablas users y access_log.
         * La tabla trips ya tiene userId, no necesita cambios de esquema.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Tabla users (T4.1)
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `users` (
                        `id` TEXT NOT NULL PRIMARY KEY,
                        `email` TEXT NOT NULL,
                        `username` TEXT NOT NULL,
                        `birthdate` INTEGER NOT NULL,
                        `address` TEXT NOT NULL DEFAULT '',
                        `country` TEXT NOT NULL DEFAULT '',
                        `phone` TEXT NOT NULL DEFAULT '',
                        `acceptEmails` INTEGER NOT NULL DEFAULT 0,
                        `createdAt` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                // Tabla access_log (T4.4)
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `access_log` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `userId` TEXT NOT NULL,
                        `event` TEXT NOT NULL,
                        `timestamp` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}