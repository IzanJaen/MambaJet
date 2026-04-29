package com.example.mambajet.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MambaJetPrefs", Context.MODE_PRIVATE)

    // ── Claves con UID para que cada usuario tenga sus propios datos ──────────
    private fun key(uid: String, field: String) = "${uid}_${field}"

    // USERNAME (por usuario)
    fun saveUsername(uid: String, name: String) =
        sharedPreferences.edit().putString(key(uid, "username"), name).apply()
    fun getUsername(uid: String): String =
        sharedPreferences.getString(key(uid, "username"), "") ?: ""

    // DATE OF BIRTH (por usuario)
    fun saveDateOfBirth(uid: String, dob: String) =
        sharedPreferences.edit().putString(key(uid, "dob"), dob).apply()
    fun getDateOfBirth(uid: String): String =
        sharedPreferences.getString(key(uid, "dob"), "") ?: ""

    // DARK MODE (global, no depende del usuario)
    fun saveDarkMode(isDark: Boolean) =
        sharedPreferences.edit().putBoolean("dark_mode", isDark).apply()
    fun isDarkMode(): Boolean =
        sharedPreferences.getBoolean("dark_mode", false)

    // APPLICATION LANGUAGE (global)
    fun saveLanguage(lang: String) =
        sharedPreferences.edit().putString("language", lang).apply()
    fun getLanguage(): String =
        sharedPreferences.getString("language", "Castellano") ?: "Castellano"
}