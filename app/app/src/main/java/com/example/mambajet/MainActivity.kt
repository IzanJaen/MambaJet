package com.example.mambajet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mambajet.ui.screens.AddTripScreen
import com.example.mambajet.ui.screens.TripDetailScreen

import com.example.mambajet.ui.theme.MambaJetTheme
import com.izanjaen.mambajet.ui.screens.HomeScreen


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
                    // Ruta de la Pantalla Principal
                    composable("home") {
                        HomeScreen(
                            onTripClick = { dest -> navController.navigate("details/$dest") },
                            onAddTripClick = { navController.navigate("add_trip") }
                        )
                    }

                    // Ruta de Detalles, en tripdetails esperamos un destino
                    composable(
                        route = "details/{dest}",
                        arguments = listOf(navArgument("dest") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val dest = backStackEntry.arguments?.getString("dest") ?: ""
                        TripDetailScreen(
                            destination = dest,
                            onBack = { navController.popBackStack() } // Vuelve atrás en la pila
                        )
                    }
                    composable("add_trip") {
                        AddTripScreen(onBack = { navController.popBackStack() })
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