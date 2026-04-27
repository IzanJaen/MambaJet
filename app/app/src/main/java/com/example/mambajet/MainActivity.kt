package com.example.mambajet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.mambajet.data.repository.TripRepositoryImpl
import com.example.mambajet.ui.screens.*
import com.example.mambajet.ui.theme.MambaJetTheme
import com.example.mambajet.ui.viewmodels.TripListViewModel
import com.example.mambajet.ui.viewmodels.ActivityViewModel
import com.example.mambajet.ui.viewmodels.SettingsViewModel // NUEVA IMPORTACIÓN

import java.util.Locale // NO OLVIDES ESTE IMPORT



class MainActivity : ComponentActivity() {

    // Así de limpio queda ahora:
    private val tripListViewModel: TripListViewModel by viewModels()
    private val activityViewModel: ActivityViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        // --- TAREA 1.5: CARGAR IDIOMA GUARDADO AL INICIAR ---
        val prefs = getSharedPreferences("MambaJetPrefs", MODE_PRIVATE)
        val savedLang = prefs.getString("language", "Castellano") ?: "Castellano"
        val localeCode = when (savedLang) {
            "English" -> "en"
            "Català" -> "ca"
            else -> "es"
        }
        val locale = Locale(localeCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        // ----------------------------------------------------

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // 1. PRIMERO: Creamos el navController (Fuera del tema)
            val navController = rememberNavController()

            // 2. SEGUNDO: Escuchamos el Modo Oscuro
            val isDarkTheme by settingsViewModel.isDarkMode.collectAsState()

            // 3. TERCERO: Aplicamos el Tema
            MambaJetTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController, // Usamos el de arriba
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                viewModel = tripListViewModel,
                                onTripClick = { tripId -> navController.navigate("details/$tripId") },
                                onAddTripClick = { navController.navigate("add_trip") },
                                isDarkTheme = isDarkTheme,
                                onGalleryClick = { navController.navigate("gallery") },
                                onTermsClick = { navController.navigate("terms") },
                                onAboutClick = { navController.navigate("about") },
                                onSettingsClick = { navController.navigate("settings") },
                                onAppSettingsClick = { navController.navigate("app_settings") }
                            )
                        }

                        composable("details/{dest}") { backStackEntry ->
                            val dest = backStackEntry.arguments?.getString("dest") ?: ""
                            val trip = tripListViewModel.trips.value.find { it.id == dest }
                            TripDetailScreen(
                                destination = trip?.title ?: dest,  // el título para mostrar
                                tripId = dest,                       // el id real para la DB
                                viewModel = activityViewModel,
                                tripViewModel = tripListViewModel,
                                onBack = { navController.popBackStack() },
                                onAddActivityClick = { navController.navigate("add_activity/$dest") },
                                onGalleryClick = { navController.navigate("gallery/$dest") },
                                onMapClick = { navController.navigate("map/$dest") },
                                onAIClick = { navController.navigate("ai/$dest") },
                                onDeleteConfirm = {
                                    tripListViewModel.deleteTrip(dest)
                                    navController.popBackStack()
                                },
                                onEditTripClick = { navController.navigate("edit_trip/$dest") },
                                onEditActivityClick = { activityId ->
                                    navController.navigate("edit_activity/$activityId")
                                }
                            )
                        }

                        composable("add_activity/{dest}") { backStackEntry ->
                            val dest = backStackEntry.arguments?.getString("dest") ?: ""
                            val trip = tripListViewModel.trips.value.find { it.id == dest || it.title == dest }
                            val tStart = trip?.startDate ?: ""
                            val tEnd = trip?.endDate ?: ""

                            AddActivityScreen(
                                tripId = dest,
                                tripStartDate = tStart,
                                tripEndDate = tEnd,
                                onBack = { navController.popBackStack() },
                                onActivitySaved = { newActivity ->
                                    // CAMBIO: Enviamos las fechas al ViewModel para validación
                                    activityViewModel.addActivity(newActivity, tStart, tEnd)
                                }
                            )
                        }

                        // 4. Editar Actividad
                        composable("edit_activity/{activityId}") { backStackEntry ->
                            val activityId = backStackEntry.arguments?.getString("activityId") ?: ""
                            val activityToEdit = activityViewModel.activities.value.find { it.id == activityId }
                            val trip = tripListViewModel.trips.value.find { it.id == activityToEdit?.tripId || it.title == activityToEdit?.tripId }
                            val tStart = trip?.startDate ?: ""
                            val tEnd = trip?.endDate ?: ""

                            EditActivityScreen(
                                activityId = activityId,
                                tripStartDate = tStart,
                                tripEndDate = tEnd,
                                viewModel = activityViewModel,
                                onBack = { navController.popBackStack() },
                                onActivityUpdated = { updatedActivity ->
                                    // CAMBIO: Enviamos las fechas al ViewModel para validación
                                    activityViewModel.updateActivity(updatedActivity, tStart, tEnd)
                                }
                            )
                        }

                        composable("add_trip") {
                            AddTripScreen(
                                onBack = { navController.popBackStack() },
                                onTripAdded = { nuevoViaje ->
                                    tripListViewModel.addTrip(nuevoViaje)
                                }
                            )
                        }

                        composable("edit_trip/{dest}") { backStackEntry ->
                            val dest = backStackEntry.arguments?.getString("dest") ?: ""
                            val tripToEdit = tripListViewModel.trips.value.find { it.title == dest || it.id == dest }

                            if (tripToEdit != null) {
                                EditTripScreen(
                                    tripToEdit = tripToEdit,
                                    onBack = { navController.popBackStack() },
                                    onTripUpdated = { viajeActualizado ->
                                        tripListViewModel.editTrip(viajeActualizado)
                                    }
                                )
                            }
                        }

                        // ... Resto de rutas
                        composable("gallery") { GalleryScreen(tripDestination = null, onBack = { navController.popBackStack() }) }
                        composable("gallery/{dest}") { backStackEntry ->
                            GalleryScreen(tripDestination = backStackEntry.arguments?.getString("dest"), onBack = { navController.popBackStack() })
                        }
                        composable("terms") { TermsAndConditionsScreen(onBack = { navController.popBackStack() }) }
                        composable("about") { AboutUsScreen(onBack = { navController.popBackStack() }) }
                        composable("map/{dest}") { backStackEntry ->
                            val dest = backStackEntry.arguments?.getString("dest") ?: ""
                            MapScreen(destination = dest, onBack = { navController.popBackStack() })
                        }
                        composable("ai/{dest}") { backStackEntry ->
                            val dest = backStackEntry.arguments?.getString("dest") ?: ""
                            AIRecommendationsScreen(destination = dest, onBack = { navController.popBackStack() })
                        }

                        // --- NUESTRAS RUTAS DE SETTINGS ACTUALIZADAS ---
                        composable("settings") {
                            UserSettingsScreen(
                                viewModel = settingsViewModel, // Pasamos el ViewModel
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
                                viewModel = settingsViewModel, // Pasamos el ViewModel
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}


