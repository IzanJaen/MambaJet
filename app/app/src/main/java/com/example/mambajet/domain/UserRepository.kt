package com.example.mambajet.domain

import kotlinx.coroutines.flow.Flow

/**
 * T4.1 — Contrato del repositorio de usuario.
 */
interface UserRepository {
    suspend fun saveUser(user: UserProfile)
    suspend fun getUserById(userId: String): UserProfile?
    fun getUserFlow(userId: String): Flow<UserProfile?>
    /** Devuelve true si el username ya está en uso por otro usuario. */
    suspend fun isUsernameTaken(username: String, excludeUserId: String = ""): Boolean
    suspend fun updateUser(user: UserProfile)
}