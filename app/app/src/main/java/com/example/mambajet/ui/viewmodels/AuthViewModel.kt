package com.example.mambajet.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mambajet.data.repository.AuthRepositoryImpl
import com.example.mambajet.domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ... (El AuthState se queda exactamente igual) ...
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    // Instanciamos el repositorio en lugar de Firebase directamente
    // (Nota: Si tu profe exige usar Hilt estrictamente, aquí iría un @Inject constructor)
    private val repository: AuthRepository = AuthRepositoryImpl()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun isUserLoggedIn(): Boolean {
        return repository.isUserLoggedIn()
    }

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Los campos no pueden estar vacíos")
            return
        }

        _authState.value = AuthState.Loading
        Log.d("MambaJetAuth", "Intentando login para: $email")

        // Usamos el repositorio en vez de Firebase
        repository.login(email, pass) { isSuccess, errorMessage ->
            if (isSuccess) {
                Log.d("MambaJetAuth", "Login exitoso")
                _authState.value = AuthState.Success
            } else {
                Log.e("MambaJetAuth", "Error en login: $errorMessage")
                _authState.value = AuthState.Error(errorMessage ?: "Error desconocido")
            }
        }
    }

    fun logout() {
        Log.d("MambaJetAuth", "Usuario cerrando sesión")
        repository.logout()
        _authState.value = AuthState.Idle
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}