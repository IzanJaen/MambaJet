package com.example.mambajet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIRecommendationsScreen(destination: String, onBack: () -> Unit) {
    val aiAccent = Color(0xFFAF4B7C)
    val mambaNeon = Color(0xFF2DB300)
    var prompt by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // DINÁMICO
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MAMBA INTELLIGENCE", letterSpacing = 2.sp, fontWeight = FontWeight.Light, fontSize = 12.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back") } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Optimización para $destination", fontWeight = FontWeight.Black, fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground)
            Text("Pide sugerencias o pide analizar tu presupuesto restante.", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = prompt, onValueChange = { prompt = it }, label = { Text("Ej: Dame un restaurante barato cerca del centro") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showResult = true },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = aiAccent)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("ANALIZAR", fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (showResult) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = aiAccent.copy(alpha = 0.1f))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Restaurant, tint = aiAccent, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sugerencia Encontrada", fontWeight = FontWeight.Bold, color = aiAccent)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Ramen Ichiran en Shibuya. Coste estimado: 12€. Encaja perfectamente en tu presupuesto diario de 45€.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = { }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = mambaNeon)) {
                            Text("CONVERTIR EN WAYPOINT", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
