package com.example.mambajet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(destination: String, onBack: () -> Unit) {
    val mambaNeon = Color(0xFF2DB300)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // DINÁMICO
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("RADAR: $destination", letterSpacing = 2.sp, fontWeight = FontWeight.Light, fontSize = 12.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back") } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding).background(MaterialTheme.colorScheme.surfaceVariant) // Simulador adaptado
        ) {
            Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Map, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(64.dp))
                Text("Módulo de Radar", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Integración GPS Pendiente", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Row(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ExtendedFloatingActionButton(
                    onClick = { },
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = mambaNeon,
                    icon = { Icon(Icons.Default.Hotel, null) },
                    text = { Text("Hoteles", fontWeight = FontWeight.Bold) }
                )
                ExtendedFloatingActionButton(
                    onClick = { },
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = mambaNeon,
                    icon = { Icon(Icons.Default.Restaurant, null) },
                    text = { Text("Gastronomía", fontWeight = FontWeight.Bold) }
                )
            }
        }
    }
}
