package com.example.mambajet.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    // Creamos el archivo físico de preferencias
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MambaJetPrefs", Context.MODE_PRIVATE)

    // USERNAME
    fun saveUsername(name: String) = sharedPreferences.edit().putString("username", name).apply()
    fun getUsername(): String = sharedPreferences.getString("username", "Izan Jaén") ?: "Izan Jaén"

    // DATE OF BIRTH (Texto)
    fun saveDateOfBirth(dob: String) = sharedPreferences.edit().putString("dob", dob).apply()
    fun getDateOfBirth(): String = sharedPreferences.getString("dob", "01/01/2000") ?: "01/01/2000"

    // DARK MODE (Booleano)
    fun saveDarkMode(isDark: Boolean) = sharedPreferences.edit().putBoolean("dark_mode", isDark).apply()
    fun isDarkMode(): Boolean = sharedPreferences.getBoolean("dark_mode", false)

    // APPLICATION LANGUAGE
    fun saveLanguage(lang: String) = sharedPreferences.edit().putString("language", lang).apply()
    fun getLanguage(): String = sharedPreferences.getString("language", "Castellano") ?: "Castellano"
}