package com.example.mambajet.data.local.db.dao

import androidx.room.*
import com.example.mambajet.data.local.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * T4.1 — DAO para operaciones CRUD sobre la tabla users.
 */
@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    /** T4.1 — Verifica si el username ya está en uso por otro usuario. */
    @Query("SELECT COUNT(*) FROM users WHERE username = :username AND id != :excludeUserId")
    suspend fun countByUsername(username: String, excludeUserId: String = ""): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserFlow(userId: String): Flow<UserEntity?>

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: String)
}