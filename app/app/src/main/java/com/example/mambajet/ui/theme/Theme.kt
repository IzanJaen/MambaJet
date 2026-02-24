package com.example.mambajet.ui.theme

import androidx.compose.ui.graphics.Color

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ui/theme/Theme.kt
private val DarkColorScheme = darkColorScheme(
    primary = MambaNeon,
    secondary = MambaGreen,
    background = JetBlack,
    surface = DarkGrey,
    onPrimary = JetBlack,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = MambaGreen,
    secondary = MambaNeon,
    background = Color.White,
    surface = LightGrey,
    onPrimary = Color.White,
    onBackground = JetBlack,
    onSurface = JetBlack
)

@Composable
fun MambaJetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Detecta el modo del sistema
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}