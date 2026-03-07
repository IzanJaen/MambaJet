package com.example.mambajet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.* // Esto trae mutableStateOf y remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mambajet.ui.screens.AIRecommendationsScreen

// ... tus imports de las pantallas ...
import com.example.mambajet.ui.screens.AddActivityScreen
import com.example.mambajet.ui.screens.AddTripScreen
import com.example.mambajet.ui.screens.GalleryScreen
import com.example.mambajet.ui.screens.TripDetailScreen
import com.example.mambajet.ui.screens.TermsAndConditionsScreen
import com.example.mambajet.ui.screens.AboutUsScreen
import com.example.mambajet.ui.screens.UserSettingsScreen
import com.example.mambajet.ui.screens.AppSettingsScreen
import com.example.mambajet.ui.theme.MambaJetTheme
import com.example.mambajet.ui.screens.HomeScreen
import com.example.mambajet.ui.screens.MapScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // --- SOLUCIÓN AQUÍ: Usamos = en vez de by ---
            val isDarkTheme = remember { mutableStateOf(false) }

            // Le pasamos el .value a tu tema
            MambaJetTheme(darkTheme = isDarkTheme.value) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            onTripClick = { dest -> navController.navigate("details/$dest") },
                            onAddTripClick = { navController.navigate("add_trip") },
                            onGalleryClick = { navController.navigate("gallery") },
                            onTermsClick = { navController.navigate("terms") },
                            onAboutClick = { navController.navigate("about") },
                            onSettingsClick = { navController.navigate("settings") },
                            onAppSettingsClick = { navController.navigate("app_settings") }
                        )
                    }

                    // (Dejo las demás rutas resumidas para que mantengas las tuyas igual)
                    composable("details/{dest}") { backStackEntry ->
                        val dest = backStackEntry.arguments?.getString("dest") ?: ""
                        TripDetailScreen(
                            destination = dest,
                            onBack = { navController.popBackStack() },
                            onAddActivityClick = { navController.navigate("add_activity") },
                            onGalleryClick = { navController.navigate("gallery/$dest") },
                            onMapClick = { navController.navigate("map/$dest") },
                            onAIClick = { navController.navigate("ai/$dest") }
                        )
                    }

                    composable("add_activity") { AddActivityScreen(onBack = { navController.popBackStack() }) }
                    composable("add_trip") { AddTripScreen(onBack = { navController.popBackStack() }) }
                    composable("gallery") { GalleryScreen(tripDestination = null, onBack = { navController.popBackStack() }) }
                    composable("gallery/{dest}") { backStackEntry ->
                        GalleryScreen(tripDestination = backStackEntry.arguments?.getString("dest"), onBack = { navController.popBackStack() })
                    }
                    composable("terms") { TermsAndConditionsScreen(onBack = { navController.popBackStack() }) }
                    composable("about") { AboutUsScreen(onBack = { navController.popBackStack() }) }

                    composable("settings") {
                        UserSettingsScreen(
                            onBack = { navController.popBackStack() },
                            onLogout = {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }

                    // --- AJUSTES DE APP (SOLUCIÓN APLICADA AQUÍ) ---
                    composable("app_settings") {
                        AppSettingsScreen(
                            isDarkTheme = isDarkTheme.value, // Le pasamos el .value
                            onThemeChange = { newTheme -> isDarkTheme.value = newTheme }, // Actualizamos el .value
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("map/{dest}") { backStackEntry ->
                        val dest = backStackEntry.arguments?.getString("dest") ?: ""
                        MapScreen(destination = dest, onBack = { navController.popBackStack() })
                    }

                    composable("ai/{dest}") { backStackEntry ->
                        val dest = backStackEntry.arguments?.getString("dest") ?: ""
                        AIRecommendationsScreen(destination = dest, onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}


