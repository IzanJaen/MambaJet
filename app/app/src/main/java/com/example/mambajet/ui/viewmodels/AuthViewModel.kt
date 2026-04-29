package com.example.mambajet.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mambajet.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun isUserLoggedIn(): Boolean = repository.isUserLoggedIn()

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Los campos no pueden estar vacíos")
            return
        }
        _authState.value = AuthState.Loading
        Log.d("MambaJetAuth", "Intentando login para: $email")
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