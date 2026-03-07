package com.example.mambajet.domain

import java.util.Date

data class GalleryImage(
    val id: String,
    val tripId: String,
    val localUri: String,
    val timestamp: Date,
    val caption: String
) {
    /**
     * Sube la imagen a la nube (MambaCloud / Firebase Storage).
     */
    fun uploadImage() {
        // @TODO Implement image upload to cloud storage
    }

    /**
     * Elimina la imagen del almacenamiento.
     */
    fun deleteImage() {
        // @TODO Implement local and cloud image deletion logic
    }
}