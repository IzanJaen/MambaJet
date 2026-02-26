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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(onBack: () -> Unit) {
    val mambaNeon = Color(0xFF2DB300)

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MAMBAJET INFO", letterSpacing = 4.sp, fontWeight = FontWeight.Light, fontSize = 12.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // --- ICONO DE LA APP ---
            Surface(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFF8F9FA),
                shadowElevation = 8.dp
            ) {
                Icon(
                    imageVector = Icons.Default.FlightTakeoff, // Tu icono simulado
                    contentDescription = "MambaJet Logo",
                    tint = mambaNeon,
                    modifier = Modifier.padding(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- TÍTULO Y VERSIÓN ---
            Text(
                text = "MambaJet",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-1).sp
            )

            Surface(
                color = mambaNeon.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            ) {
                Text(
                    text = "Versión 0.4.0",
                    color = mambaNeon,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            // --- DESCRIPCIÓN ---
            Text(
                text = "El ecosistema definitivo para la planificación de misiones y logística de viajes. Diseñado para la élite, construido para la eficiencia.",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- TARJETA DE INFORMACIÓN TÉCNICA ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "INFORMACIÓN TÉCNICA",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Fila 1: Sprint
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(8.dp), color = mambaNeon.copy(alpha = 0.1f), modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.Code, contentDescription = null, tint = mambaNeon, modifier = Modifier.padding(8.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Fase de Desarrollo", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Sprint 1 Activo", color = Color.Gray, fontSize = 12.sp)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.3f))

                    // Fila 2: Equipo
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(8.dp), color = mambaNeon.copy(alpha = 0.1f), modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.Engineering, contentDescription = null, tint = mambaNeon, modifier = Modifier.padding(8.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Equipo de Desarrollo", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Izan Jaén (Solo Developer)", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}