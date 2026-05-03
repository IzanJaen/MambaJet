package com.example.mambajet.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mambajet.R
import com.example.mambajet.domain.AccessLogRepository
import com.example.mambajet.domain.AuthRepository
import com.example.mambajet.domain.UserProfile
import com.example.mambajet.domain.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    // En lugar de String con el mensaje directo, guardamos el ResId para que la UI lo resuelva
    data class Error(val messageResId: Int, val fallbackMessage: String = "") : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userRepository: UserRepository,
    private val accessLogRepository: AccessLogRepository
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
            _authState.value = AuthState.Error(R.string.error_fields_empty)
            return
        }
        _authState.value = AuthState.Loading
        Log.d("AuthViewModel", "Intentando login: $email")
        repository.login(email, pass) { isSuccess, errorMessage, exception ->
            if (isSuccess) {
                Log.d("AuthViewModel", "Login exitoso")
                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@login
                viewModelScope.launch {
                    accessLogRepository.logLogin(uid)
                    Log.d("AuthViewModel", "Evento LOGIN registrado para uid: $uid")
                }
                _authState.value = AuthState.Success
            } else {
                Log.e("AuthViewModel", "Error login: $errorMessage")
                _authState.value = AuthState.Error(mapFirebaseLoginError(exception))
            }
        }
    }

    // ── REGISTER ───────────────────────────────────────────────────────────────
    fun register(
        email: String,
        password: String,
        confirmPassword: String,
        username: String,
        birthdate: Long = 0L,
        address: String = "",
        country: String = "",
        phone: String = "",
        acceptEmails: Boolean = false
    ) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank() || username.isBlank()) {
            _registerState.value = AuthState.Error(R.string.error_fields_empty)
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _registerState.value = AuthState.Error(R.string.error_invalid_email)
            return
        }
        if (password.length < 6) {
            _registerState.value = AuthState.Error(R.string.error_weak_password)
            return
        }
        if (password != confirmPassword) {
            _registerState.value = AuthState.Error(R.string.error_passwords_no_match)
            return
        }

        _registerState.value = AuthState.Loading

        viewModelScope.launch {
            val taken = userRepository.isUsernameTaken(username)
            if (taken) {
                Log.w("AuthViewModel", "Username '$username' ya en uso")
                _registerState.value = AuthState.Error(R.string.error_username_taken)
                return@launch
            }

            Log.d("AuthViewModel", "Intentando registro: $email")
            repository.register(email, password) { isSuccess, errorMessage, exception ->
                if (isSuccess) {
                    repository.sendEmailVerification { sent, _ ->
                        if (sent) Log.d("AuthViewModel", "Email de verificación enviado")
                        else Log.w("AuthViewModel", "No se pudo enviar verificación")
                    }
                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@register
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
                        accessLogRepository.logLogin(uid)
                    }
                    Log.d("AuthViewModel", "Registro exitoso")
                    _registerState.value = AuthState.Success
                } else {
                    Log.e("AuthViewModel", "Error registro: $errorMessage")
                    _registerState.value = AuthState.Error(mapFirebaseRegisterError(exception))
                }
            }
        }
    }

    // ── RECOVER PASSWORD ───────────────────────────────────────────────────────
    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _resetState.value = AuthState.Error(R.string.error_email_empty)
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _resetState.value = AuthState.Error(R.string.error_invalid_email)
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
                _resetState.value = AuthState.Error(R.string.error_generic)
            }
        }
    }

    // ── LOGOUT ─────────────────────────────────────────────────────────────────
    fun logout() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("AuthViewModel", "Usuario cerrando sesión: $uid")
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

    // ── ERROR MAPPERS ──────────────────────────────────────────────────────────
    private fun mapFirebaseLoginError(exception: Exception?): Int {
        return when (exception) {
            is FirebaseAuthInvalidCredentialsException -> R.string.error_wrong_credentials
            is FirebaseAuthInvalidUserException -> R.string.error_user_not_found
            else -> R.string.error_generic
        }
    }

    private fun mapFirebaseRegisterError(exception: Exception?): Int {
        return when (exception) {
            is FirebaseAuthUserCollisionException -> R.string.error_email_in_use
            is FirebaseAuthWeakPasswordException -> R.string.error_weak_password
            is FirebaseAuthInvalidCredentialsException -> R.string.error_invalid_email
            else -> R.string.error_generic
        }
    }
}