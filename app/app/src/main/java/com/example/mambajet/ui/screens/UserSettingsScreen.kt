package com.example.mambajet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsScreen(onBack: () -> Unit, onLogout: () -> Unit) {
    val mambaNeon = Color(0xFF2DB300)
    val dangerRed = Color(0xFFFF3B30)

    // Estado para mostrar u ocultar el mensaje de confirmación
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PERFIL", letterSpacing = 4.sp, fontWeight = FontWeight.Light, fontSize = 12.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFF8F9FA))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- CABECERA SUPERIOR (Icono limpio) ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(mambaNeon.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(50.dp), tint = mambaNeon)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("MAMBA ELITE MEMBER", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = mambaNeon, letterSpacing = 1.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- TARJETA DE DATOS DEL PERFIL ---
            Text(
                text = "AJUSTES DE PERFIL",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = Color.Gray,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column {
                    ProfileFieldItem(label = "Nombre", value = "Izan Jaén", mambaNeon) { /* Editar nombre */ }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = Color.LightGray.copy(alpha = 0.3f))

                    ProfileFieldItem(label = "Correo electrónico", value = "izan@mambajet.com", mambaNeon) { /* Editar correo */ }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = Color.LightGray.copy(alpha = 0.3f))

                    ProfileFieldItem(label = "Contraseña", value = "••••••••", mambaNeon) { /* Editar contraseña */ }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- BOTÓN DE CERRAR SESIÓN ---
            // Le quitamos el padding interior para que no se "achafe" y usamos un Spacer debajo
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // Altura perfecta
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = dangerRed, contentColor = Color.White)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("CERRAR SESIÓN", fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }

            // Este Spacer es el que da el margen por debajo sin encoger el botón
            Spacer(modifier = Modifier.height(32.dp))
        }

        // --- DIÁLOGO DE CONFIRMACIÓN ---
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = {
                    Text("Cerrar Sesión", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                text = {
                    Text("¿Estás seguro de que quieres salir de tu cuenta de MambaJet?")
                },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                        onLogout() // Ejecuta el cierre de sesión real
                    }) {
                        Text("SALIR", color = dangerRed, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("CANCELAR", color = Color.Gray, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

// --- COMPONENTE PARA CADA FILA (Nombre, Correo, etc.) ---
@Composable
fun ProfileFieldItem(label: String, value: String, accentColor: Color, onEditClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
        }

        // Botón de texto "Editar" a la derecha
        Text(
            text = "Editar",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )
    }
}
