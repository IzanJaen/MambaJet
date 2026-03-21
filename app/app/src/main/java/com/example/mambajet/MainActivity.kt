package com.example.mambajet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels // IMPORTANTE: Nueva importación
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.mambajet.data.repository.TripRepositoryImpl
import com.example.mambajet.ui.screens.*
import com.example.mambajet.ui.theme.MambaJetTheme
import com.example.mambajet.ui.viewmodels.TripListViewModel

class MainActivity : ComponentActivity() {

    // LA FORMA OFICIAL DE ANDROID: Esto sobrevive a giros de pantalla y recargas
    private val tripListViewModel: TripListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MambaJetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                viewModel = tripListViewModel, // Usamos la variable indestructible
                                onTripClick = { dest -> navController.navigate("details/$dest") },
                                onAddTripClick = { navController.navigate("add_trip") },
                                onGalleryClick = { navController.navigate("gallery") },
                                onTermsClick = { navController.navigate("terms") },
                                onAboutClick = { navController.navigate("about") },
                                onSettingsClick = { navController.navigate("settings") },
                                onAppSettingsClick = { navController.navigate("app_settings") }
                            )
                        }

                        // ... (EL RESTO DE TUS RUTAS SE QUEDAN EXACTAMENTE IGUAL) ...
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

                        composable("add_trip") {
                            AddTripScreen(
                                onBack = { navController.popBackStack() },
                                onTripAdded = { nuevoViaje ->
                                    tripListViewModel.addTrip(nuevoViaje)
                                }
                            )
                        }

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

                        composable("app_settings") {
                            AppSettingsScreen(
                                isDarkTheme = false,
                                onThemeChange = { },
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
}


