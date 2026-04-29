package com.example.mambajet.domain

interface AuthRepository {
    fun isUserLoggedIn(): Boolean
    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit)
    fun logout()
    fun register(email: String, password: String, onComplete: (Boolean, String?) -> Unit)
    fun sendEmailVerification(onComplete: (Boolean, String?) -> Unit)
    fun sendPasswordResetEmail(email: String, onComplete: (Boolean, String?) -> Unit)
    fun isEmailVerified(): Boolean
}