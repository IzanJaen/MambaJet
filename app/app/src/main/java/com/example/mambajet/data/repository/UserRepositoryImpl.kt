package com.example.mambajet.data.repository

import android.util.Log
import com.example.mambajet.data.local.db.dao.UserDao
import com.example.mambajet.data.local.db.mapper.toDomain
import com.example.mambajet.data.local.db.mapper.toEntity
import com.example.mambajet.domain.UserProfile
import com.example.mambajet.domain.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * T4.1 — Implementación del repositorio de usuario usando Room.
 */
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }

    override suspend fun saveUser(user: UserProfile) {
        Log.d(TAG, "Guardando usuario en DB local: ${user.email}")
        userDao.insertUser(user.toEntity())
    }

    override suspend fun getUserById(userId: String): UserProfile? {
        Log.d(TAG, "Buscando usuario por ID: $userId")
        return userDao.getUserById(userId)?.toDomain()
    }

    override fun getUserFlow(userId: String): Flow<UserProfile?> {
        return userDao.getUserFlow(userId).map { it?.toDomain() }
    }

    override suspend fun isUsernameTaken(username: String, excludeUserId: String): Boolean {
        val count = userDao.countByUsername(username, excludeUserId)
        Log.d(TAG, "Username '$username' en uso: $count veces")
        return count > 0
    }

    override suspend fun updateUser(user: UserProfile) {
        Log.d(TAG, "Actualizando usuario en DB local: ${user.email}")
        userDao.updateUser(user.toEntity())
    }
}