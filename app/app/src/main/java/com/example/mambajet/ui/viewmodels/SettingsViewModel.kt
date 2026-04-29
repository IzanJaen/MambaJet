package com.example.mambajet.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mambajet.data.local.PreferencesManager
import com.example.mambajet.domain.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository
) : ViewModel() {

    companion object {
        private const val TAG = "SettingsViewModel"
    }

    private val prefsManager = PreferencesManager(context)
    private val uid: String get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _dateOfBirth = MutableStateFlow("")
    val dateOfBirth: StateFlow<String> = _dateOfBirth.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _isDarkMode = MutableStateFlow(prefsManager.isDarkMode())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _language = MutableStateFlow(prefsManager.getLanguage())
    val language: StateFlow<String> = _language.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        val currentUid = uid
        if (currentUid.isBlank()) return

        viewModelScope.launch {
            val profile = userRepository.getUserById(currentUid)
            if (profile != null) {
                Log.d(TAG, "Perfil cargado de BD para uid=$currentUid: ${profile.email}")

                // Email siempre viene de la BD
                _email.value = profile.email

                // Username: primero prefs, si vacío usar BD
                val savedName = prefsManager.getUsername(currentUid)
                _username.value = if (savedName.isNotBlank()) savedName else {
                    prefsManager.saveUsername(currentUid, profile.username)
                    profile.username
                }

                // Fecha: primero prefs, si vacío usar BD
                val savedDob = prefsManager.getDateOfBirth(currentUid)
                _dateOfBirth.value = if (savedDob.isNotBlank()) savedDob else {
                    if (profile.birthdate > 0L) {
                        val formatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .format(Date(profile.birthdate))
                        prefsManager.saveDateOfBirth(currentUid, formatted)
                        formatted
                    } else ""
                }
            } else {
                // Usuario no encontrado en BD (p.ej. logueo sin registro previo en la app)
                // Al menos mostramos el email de Firebase
                _email.value = FirebaseAuth.getInstance().currentUser?.email ?: ""
                _username.value = prefsManager.getUsername(currentUid)
                _dateOfBirth.value = prefsManager.getDateOfBirth(currentUid)
                Log.w(TAG, "Perfil no encontrado en BD local para uid=$currentUid")
            }
        }
    }

    fun updateUsername(newName: String) {
        val currentUid = uid
        _username.value = newName
        prefsManager.saveUsername(currentUid, newName)
        // Actualizar también en la BD
        viewModelScope.launch {
            val profile = userRepository.getUserById(currentUid) ?: return@launch
            userRepository.updateUser(profile.copy(username = newName))
            Log.d(TAG, "Username actualizado en BD: $newName")
        }
    }

    fun updateDateOfBirth(newDob: String) {
        val currentUid = uid
        _dateOfBirth.value = newDob
        prefsManager.saveDateOfBirth(currentUid, newDob)
        // Actualizar también en la BD
        viewModelScope.launch {
            val profile = userRepository.getUserById(currentUid) ?: return@launch
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val timestamp = try { sdf.parse(newDob)?.time ?: profile.birthdate }
            catch (e: Exception) { profile.birthdate }
            userRepository.updateUser(profile.copy(birthdate = timestamp))
            Log.d(TAG, "Fecha de nacimiento actualizada en BD: $newDob")
        }
    }

    fun updateDarkMode(isDark: Boolean) {
        _isDarkMode.value = isDark
        prefsManager.saveDarkMode(isDark)
    }

    fun updateLanguage(newLang: String) {
        _language.value = newLang
        prefsManager.saveLanguage(newLang)
    }
}