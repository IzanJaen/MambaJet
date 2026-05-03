package com.example.mambajet.data.repository

import android.util.Log
import com.example.mambajet.domain.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun isEmailVerified(): Boolean {
        return auth.currentUser?.isEmailVerified == true
    }

    override fun login(email: String, password: String, onComplete: (Boolean, String?, Exception?) -> Unit) {
        Log.d(TAG, "Intentando login: $email")
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Login exitoso para: $email")
                    onComplete(true, null, null)
                } else {
                    Log.e(TAG, "Error login: ${task.exception?.message}")
                    onComplete(false, task.exception?.message ?: "Error desconocido", task.exception)
                }
            }
    }

    override fun register(email: String, password: String, onComplete: (Boolean, String?, Exception?) -> Unit) {
        Log.d(TAG, "Intentando registro: $email")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Registro exitoso para: $email")
                    onComplete(true, null, null)
                } else {
                    Log.e(TAG, "Error registro: ${task.exception?.message}")
                    onComplete(false, task.exception?.message ?: "Error desconocido", task.exception)
                }
            }
    }

    override fun sendEmailVerification(onComplete: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            Log.e(TAG, "No hay usuario para verificar email")
            onComplete(false, "No hay usuario autenticado")
            return
        }
        Log.d(TAG, "Enviando email de verificación a: ${user.email}")
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email de verificación enviado")
                    onComplete(true, null)
                } else {
                    Log.e(TAG, "Error al enviar verificación: ${task.exception?.message}")
                    onComplete(false, task.exception?.message ?: "Error al enviar email")
                }
            }
    }

    override fun sendPasswordResetEmail(email: String, onComplete: (Boolean, String?) -> Unit) {
        Log.d(TAG, "Enviando email de recuperación a: $email")
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email de recuperación enviado a: $email")
                    onComplete(true, null)
                } else {
                    Log.e(TAG, "Error al enviar recuperación: ${task.exception?.message}")
                    onComplete(false, task.exception?.message ?: "Error al enviar email")
                }
            }
    }

    override fun logout() {
        Log.d(TAG, "Cerrando sesión: ${auth.currentUser?.email}")
        auth.signOut()
    }
}