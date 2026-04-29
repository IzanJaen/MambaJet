package com.example.mambajet.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mambajet.domain.AccessLogRepository
import com.example.mambajet.domain.AuthRepository
import com.example.mambajet.domain.UserProfile
import com.example.mambajet.domain.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userRepository: UserRepository,       // T4.1
    private val accessLogRepository: AccessLogRepository // T4.4
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState

    private val _resetState = MutableStateFlow<AuthState>(AuthState.Idle)
    val resetState: StateFlow<AuthState> = _resetState

    fun isUserLoggedIn(): Boolean = repository.isUserLoggedIn()
    fun isEmailVerified(): Boolean = repository.isEmailVerified()
    fun getCurrentUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid

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
                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@login
                // T4.4 — Persistir evento LOGIN
                viewModelScope.launch {
                    accessLogRepository.logLogin(uid)
                    Log.d("AuthViewModel", "Evento LOGIN registrado para uid: $uid")
                }
                _authState.value = AuthState.Success
            } else {
                Log.e("AuthViewModel", "Error login: $errorMessage")
                _authState.value = AuthState.Error(errorMessage ?: "Error desconocido")
            }
        }
    }

    // ── REGISTER ───────────────────────────────────────────────────────────────
    /**
     * T4.1 — Registro con persistencia de usuario en DB local.
     * Recibe todos los campos del perfil y verifica que el username no esté en uso.
     */
    fun register(
        email: String,
        password: String,
        confirmPassword: String,
        username: String,
        birthdate: Long,
        address: String,
        country: String,
        phone: String,
        acceptEmails: Boolean
    ) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank() || username.isBlank()) {
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

        // T4.1 — Verificar username único antes de registrar en Firebase
        viewModelScope.launch {
            val taken = userRepository.isUsernameTaken(username)
            if (taken) {
                Log.w("AuthViewModel", "Username '$username' ya en uso")
                _registerState.value = AuthState.Error("El nombre de usuario ya está en uso")
                return@launch
            }

            Log.d("AuthViewModel", "Intentando registro: $email")
            repository.register(email, password) { isSuccess, errorMessage ->
                if (isSuccess) {
                    repository.sendEmailVerification { sent, _ ->
                        if (sent) Log.d("AuthViewModel", "Email de verificación enviado")
                        else Log.w("AuthViewModel", "No se pudo enviar verificación")
                    }
                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@register
                    // T4.1 — Guardar perfil de usuario en DB local
                    viewModelScope.launch {
                        val userProfile = UserProfile(
                            id = uid,
                            email = email,
                            username = username,
                            birthdate = birthdate,
                            address = address,
                            country = country,
                            phone = phone,
                            acceptEmails = acceptEmails
                        )
                        userRepository.saveUser(userProfile)
                        Log.d("AuthViewModel", "Perfil de usuario guardado en DB local: $uid")
                        // T4.4 — Primer acceso como LOGIN
                        accessLogRepository.logLogin(uid)
                    }
                    Log.d("AuthViewModel", "Registro exitoso")
                    _registerState.value = AuthState.Success
                } else {
                    Log.e("AuthViewModel", "Error registro: $errorMessage")
                    _registerState.value = AuthState.Error(errorMessage ?: "Error desconocido")
                }
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
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("AuthViewModel", "Usuario cerrando sesión: $uid")
        // T4.4 — Persistir evento LOGOUT antes de cerrar sesión
        if (uid != null) {
            viewModelScope.launch {
                accessLogRepository.logLogout(uid)
                Log.d("AuthViewModel", "Evento LOGOUT registrado para uid: $uid")
            }
        }
        repository.logout()
        _authState.value = AuthState.Idle
    }

    fun resetState() { _authState.value = AuthState.Idle }
    fun resetRegisterState() { _registerState.value = AuthState.Idle }
    fun resetPasswordResetState() { _resetState.value = AuthState.Idle }
}