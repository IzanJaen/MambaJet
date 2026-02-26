package com.example.mambajet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mambajet.ui.screens.AboutUsScreen

import com.example.mambajet.ui.screens.AddActivityScreen
import com.example.mambajet.ui.screens.AddTripScreen
import com.example.mambajet.ui.screens.GalleryScreen
import com.example.mambajet.ui.screens.TripDetailScreen
import com.example.mambajet.ui.screens.TermsAndConditionsScreen // AÑADIDO
import com.example.mambajet.ui.theme.MambaJetTheme
import com.example.mambajet.ui.screens.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MambaJetTheme {
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
                            onTermsClick = { navController.navigate("terms") }, // <--- AÑADIDO
                            onAboutClick = { navController.navigate("about") }
                        )
                    }

                    composable("details/{dest}") { backStackEntry ->
                        val dest = backStackEntry.arguments?.getString("dest") ?: ""
                        TripDetailScreen(
                            destination = dest,
                            onBack = { navController.popBackStack() },
                            onAddActivityClick = { navController.navigate("add_activity") },
                            onGalleryClick = { navController.navigate("gallery/$dest") }
                        )
                    }

                    composable("add_activity") {
                        AddActivityScreen(onBack = { navController.popBackStack() })
                    }

                    composable("add_trip") {
                        AddTripScreen(onBack = { navController.popBackStack() })
                    }

                    composable("gallery") {
                        GalleryScreen(tripDestination = null, onBack = { navController.popBackStack() })
                    }

                    composable("gallery/{dest}") { backStackEntry ->
                        val dest = backStackEntry.arguments?.getString("dest")
                        GalleryScreen(tripDestination = dest, onBack = { navController.popBackStack() })
                    }

                    // --- NUEVA RUTA PARA TÉRMINOS ---
                    composable("terms") {
                        TermsAndConditionsScreen(onBack = { navController.popBackStack() })
                    }
                    composable("about") {
                        AboutUsScreen(onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MambaJetTheme {
        AddTripScreen {  }
        }
}