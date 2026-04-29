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

    // Estado separado para registro
    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState

    // Estado separado para recuperar contraseña
    private val _resetState = MutableStateFlow<AuthState>(AuthState.Idle)
    val resetState: StateFlow<AuthState> = _resetState

    fun isUserLoggedIn(): Boolean = repository.isUserLoggedIn()

    fun isEmailVerified(): Boolean = repository.isEmailVerified()

    // ── LOGIN ──────────────────────────────────────────────────────────────────
    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Los campos no pueden estar vacíos")
            return
        }
        _authState.value = AuthState.Loading
        Log.d("AuthViewModel", "Intentando login: $email")
        repository.login(email, pass) { isSuccess, errorMessage ->
            if (isSuccess) {
                Log.d("AuthViewModel", "Login exitoso")
                _authState.value = AuthState.Success
            } else {
                Log.e("AuthViewModel", "Error login: $errorMessage")
                _authState.value = AuthState.Error(errorMessage ?: "Error desconocido")
            }
        }
    }

    // ── REGISTER ───────────────────────────────────────────────────────────────
    fun register(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _registerState.value = AuthState.Error("Todos los campos son obligatorios")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _registerState.value = AuthState.Error("El correo no tiene formato válido")
            return
        }
        if (password.length < 6) {
            _registerState.value = AuthState.Error("La contraseña debe tener mínimo 6 caracteres")
            return
        }
        if (password != confirmPassword) {
            _registerState.value = AuthState.Error("Las contraseñas no coinciden")
            return
        }
        _registerState.value = AuthState.Loading
        Log.d("AuthViewModel", "Intentando registro: $email")
        repository.register(email, password) { isSuccess, errorMessage ->
            if (isSuccess) {
                // Enviar email de verificación automáticamente tras registrarse
                repository.sendEmailVerification { sent, _ ->
                    if (sent) Log.d("AuthViewModel", "Email de verificación enviado")
                    else Log.w("AuthViewModel", "No se pudo enviar verificación")
                }
                Log.d("AuthViewModel", "Registro exitoso")
                _registerState.value = AuthState.Success
            } else {
                Log.e("AuthViewModel", "Error registro: $errorMessage")
                _registerState.value = AuthState.Error(errorMessage ?: "Error desconocido")
            }
        }
    }

    // ── RECOVER PASSWORD ───────────────────────────────────────────────────────
    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _resetState.value = AuthState.Error("Introduce tu correo electrónico")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _resetState.value = AuthState.Error("El correo no tiene formato válido")
            return
        }
        _resetState.value = AuthState.Loading
        Log.d("AuthViewModel", "Enviando reset a: $email")
        repository.sendPasswordResetEmail(email) { isSuccess, errorMessage ->
            if (isSuccess) {
                Log.d("AuthViewModel", "Reset enviado a: $email")
                _resetState.value = AuthState.Success
            } else {
                Log.e("AuthViewModel", "Error reset: $errorMessage")
                _resetState.value = AuthState.Error(errorMessage ?: "Error desconocido")
            }
        }
    }

    // ── LOGOUT ─────────────────────────────────────────────────────────────────
    fun logout() {
        Log.d("AuthViewModel", "Usuario cerrando sesión")
        repository.logout()
        _authState.value = AuthState.Idle
    }

    fun resetState() { _authState.value = AuthState.Idle }
    fun resetRegisterState() { _registerState.value = AuthState.Idle }
    fun resetPasswordResetState() { _resetState.value = AuthState.Idle }
}