package com.example.mambajet.di

import android.content.Context
import androidx.room.Room
import com.example.mambajet.data.local.db.AppDatabase
import com.example.mambajet.data.local.db.dao.AccessLogDao
import com.example.mambajet.data.local.db.dao.ActivityDao
import com.example.mambajet.data.local.db.dao.TripDao
import com.example.mambajet.data.local.db.dao.UserDao
import com.example.mambajet.data.repository.AccessLogRepositoryImpl
import com.example.mambajet.data.repository.ActivityRepositoryImpl
import com.example.mambajet.data.repository.AuthRepositoryImpl
import com.example.mambajet.data.repository.TripRepositoryImpl
import com.example.mambajet.data.repository.UserRepositoryImpl
import com.example.mambajet.domain.AccessLogRepository
import com.example.mambajet.domain.ActivityRepository
import com.example.mambajet.domain.AuthRepository
import com.example.mambajet.domain.TripRepository
import com.example.mambajet.domain.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mambajet_database"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2) // T4 — migración a versión 2
            .build()
    }

    @Provides
    @Singleton
    fun provideTripDao(db: AppDatabase): TripDao = db.tripDao()

    @Provides
    @Singleton
    fun provideActivityDao(db: AppDatabase): ActivityDao = db.activityDao()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()          // T4.1

    @Provides
    @Singleton
    fun provideAccessLogDao(db: AppDatabase): AccessLogDao = db.accessLogDao() // T4.4

    @Provides
    @Singleton
    fun provideTripRepository(tripDao: TripDao): TripRepository {
        return TripRepositoryImpl(tripDao)
    }

    @Provides
    @Singleton
    fun provideActivityRepository(activityDao: ActivityDao): ActivityRepository {
        return ActivityRepositoryImpl(activityDao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository { // T4.1
        return UserRepositoryImpl(userDao)
    }

    @Provides
    @Singleton
    fun provideAccessLogRepository(accessLogDao: AccessLogDao): AccessLogRepository { // T4.4
        return AccessLogRepositoryImpl(accessLogDao)
    }
}