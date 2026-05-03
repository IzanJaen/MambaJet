package com.example.mambajet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mambajet.ui.screens.*
import com.example.mambajet.ui.theme.MambaJetTheme
import com.example.mambajet.ui.viewmodels.TripListViewModel
import com.example.mambajet.ui.viewmodels.ActivityViewModel
import com.example.mambajet.ui.viewmodels.SettingsViewModel
import com.example.mambajet.ui.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val tripListViewModel: TripListViewModel by viewModels()
    private val activityViewModel: ActivityViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

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

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val isDarkTheme by settingsViewModel.isDarkMode.collectAsState()

            val user = remember { FirebaseAuth.getInstance().currentUser }
            val startRoute = if (user != null) "home" else "login"

            android.util.Log.d("MambaJet_Nav", "Usuario actual: ${user?.email ?: "Nadie"}")
            android.util.Log.d("MambaJet_Nav", "Ruta de inicio decidida: $startRoute")

            MambaJetTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = startRoute
                    ) {
                        composable("home") {
                            // Informar al ViewModel del usuario actual
                            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                            LaunchedEffect(uid) {
                                tripListViewModel.setCurrentUser(uid)
                                // FIX: recargar perfil del usuario actual al entrar en home.
                                // Esto garantiza que el nombre mostrado en el botón de perfil
                                // se actualice siempre que cambie el usuario autenticado.
                                settingsViewModel.loadUserProfile()
                            }
                            HomeScreen(
                                viewModel = tripListViewModel,
                                settingsViewModel = settingsViewModel,
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
                                destination = trip?.title ?: dest,
                                tripId = dest,
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

                        composable("login") {
                            LoginScreen(
                                viewModel = authViewModel,
                                onLoginSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = {
                                    navController.navigate("register")
                                },
                                onNavigateToForgotPassword = {
                                    navController.navigate("forgot_password")
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
                                    activityViewModel.addActivity(newActivity, tStart, tEnd)
                                }
                            )
                        }

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

                        composable("settings") {
                            LaunchedEffect(Unit) { settingsViewModel.loadUserProfile() }
                            UserSettingsScreen(
                                viewModel = settingsViewModel,
                                onBack = { navController.popBackStack() },
                                onLogout = {
                                    authViewModel.logout()
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("app_settings") {
                            AppSettingsScreen(
                                viewModel = settingsViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                viewModel = authViewModel,
                                onRegisterSuccess = {
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("forgot_password") {
                            ForgotPasswordScreen(
                                viewModel = authViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}


