package com.example.mambajet.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mambajet.data.local.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsManager = PreferencesManager(application)

    // Cargamos los valores iniciales directamente de la memoria física
    private val _username = MutableStateFlow(prefsManager.getUsername())
    val username: StateFlow<String> = _username.asStateFlow()

    private val _dateOfBirth = MutableStateFlow(prefsManager.getDateOfBirth())
    val dateOfBirth: StateFlow<String> = _dateOfBirth.asStateFlow()

    private val _isDarkMode = MutableStateFlow(prefsManager.isDarkMode())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _language = MutableStateFlow(prefsManager.getLanguage())
    val language: StateFlow<String> = _language.asStateFlow()

    // Funciones para actualizar estado y guardar en disco a la vez
    fun updateUsername(newName: String) {
        _username.value = newName
        prefsManager.saveUsername(newName)
    }

    fun updateDateOfBirth(newDob: String) {
        _dateOfBirth.value = newDob
        prefsManager.saveDateOfBirth(newDob)
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