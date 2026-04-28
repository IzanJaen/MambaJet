package com.example.mambajet.domain

interface AuthRepository {
    fun isUserLoggedIn(): Boolean
    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit)
    fun logout()
}