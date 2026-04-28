package com.example.mambajet.data.repository

import com.example.mambajet.domain.AuthRepository
import com.google.firebase.auth.FirebaseAuth

class AuthRepositoryImpl : AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Éxito: devolvemos true y sin mensaje de error
                    onComplete(true, null)
                } else {
                    // Fallo: devolvemos false y el mensaje de error
                    onComplete(false, task.exception?.message ?: "Error desconocido")
                }
            }
    }

    override fun logout() {
        auth.signOut()
    }
}