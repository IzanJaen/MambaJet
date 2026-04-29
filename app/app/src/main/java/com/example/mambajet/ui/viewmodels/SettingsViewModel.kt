package com.example.mambajet.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.mambajet.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val prefsManager = PreferencesManager(context)

    private val _username = MutableStateFlow(prefsManager.getUsername())
    val username: StateFlow<String> = _username.asStateFlow()

    private val _dateOfBirth = MutableStateFlow(prefsManager.getDateOfBirth())
    val dateOfBirth: StateFlow<String> = _dateOfBirth.asStateFlow()

    private val _isDarkMode = MutableStateFlow(prefsManager.isDarkMode())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _language = MutableStateFlow(prefsManager.getLanguage())
    val language: StateFlow<String> = _language.asStateFlow()

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