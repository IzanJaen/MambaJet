package com.example.mambajet.domain

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val membershipTier: String,
    val avatarUrl: String
) {
    /**
     * Actualiza la información pública del perfil del usuario.
     */
    fun updateProfile() {
        // @TODO Implement profile update logic (API call / local DB sync)
    }

    /**
     * Cambia la imagen de avatar del usuario.
     */
    fun changeAvatar() {
        // @TODO Implement avatar change logic (Image picker & upload)
    }

    /**
     * Elimina por completo la cuenta del usuario y sus datos asociados.
     */
    fun deleteAccount() {
        // @TODO Implement account deletion logic ensuring GDPR compliance
    }
}
