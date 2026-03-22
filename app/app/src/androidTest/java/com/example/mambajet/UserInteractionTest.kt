package com.example.mambajet

import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mambajet.ui.screens.AddTripScreen
import org.junit.Rule
import org.junit.Test

class UserInteractionTest {

    // Regla obligatoria para poder interactuar con pantallas de Jetpack Compose
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun simulateUserInteraction_missingFieldsLogsError() {
        Log.d("MambaTest", "Iniciando simulación de usuario: Pantalla Añadir Viaje")

        // MAGIA ANTI-ERRORES: Obtenemos el texto exacto según el idioma del emulador
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val tripNameLabel = context.getString(R.string.trip_name)
        val missingFieldsWarning = context.getString(R.string.missing_fields)

        // 1. CARGAMOS LA PANTALLA
        composeTestRule.setContent {
            AddTripScreen(
                onBack = {},
                onTripAdded = {}
            )
        }

        // 2. SIMULAMOS INTERACCIÓN
        Log.d("MambaTest", "Simulando interacción: Escribiendo 'Viaje a Roma'")

        // Ahora busca el campo usando la variable dinámica (sea inglés, español o catalán)
        composeTestRule.onNodeWithText(tripNameLabel).performTextInput("Viaje a Roma")

        // 3. COMPROBACIÓN
        try {
            // Comprobamos el aviso dinámico también
            composeTestRule.onNodeWithText(missingFieldsWarning).assertIsDisplayed()
            Log.d("MambaTest", "ÉXITO: La UI detectó el comportamiento inesperado y mostró la advertencia al usuario.")
        } catch (e: AssertionError) {
            Log.e("MambaTest", "FALLO CRÍTICO: El usuario dejó campos vacíos pero la UI no le avisó.")
            throw e
        }
    }
}