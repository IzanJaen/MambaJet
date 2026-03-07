package com.example.mambajet.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsScreen(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    val mambaNeon = Color(0xFF2DB300)

    // Estados para los ajustes
    var notificationsEnabled by remember { mutableStateOf(true) }
    var hapticFeedbackEnabled by remember { mutableStateOf(true) }

    // Estados para el idioma
    var languageExpanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("Castellano") }
    val languages = listOf("Català", "Castellano", "English")

    Scaffold(
        // Usamos el color de fondo del tema para que reaccione al modo oscuro
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("APP SETTINGS", letterSpacing = 4.sp, fontWeight = FontWeight.Light, fontSize = 12.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
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

            Text(
                text = "PREFERENCIAS",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = Color.Gray,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
            )

            // --- TARJETA 1: APARIENCIA E IDIOMA ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column {
                    // MODO OSCURO (Funcional)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode, contentDescription = null, tint = mambaNeon)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Tema Oscuro", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { onThemeChange(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = mambaNeon)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = Color.LightGray.copy(alpha = 0.2f))

                    // IDIOMA (Selector)
                    ExposedDropdownMenuBox(
                        expanded = languageExpanded,
                        onExpandedChange = { languageExpanded = !languageExpanded }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { languageExpanded = true }
                                .padding(horizontal = 20.dp, vertical = 20.dp)
                                .menuAnchor(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = mambaNeon)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Idioma", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(selectedLanguage, fontSize = 14.sp, color = Color.Gray)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                        }

                        ExposedDropdownMenu(
                            expanded = languageExpanded,
                            onDismissRequest = { languageExpanded = false }
                        ) {
                            languages.forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang) },
                                    onClick = {
                                        selectedLanguage = lang
                                        languageExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SISTEMA",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = Color.Gray,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
            )

            // --- TARJETA 2: NOTIFICACIONES Y OTROS ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column {
                    // NOTIFICACIONES PUSH
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = mambaNeon)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Notificaciones Push", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = mambaNeon)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = Color.LightGray.copy(alpha = 0.2f))

                    // VIBRACIÓN HÁPTICA
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Vibration, contentDescription = null, tint = mambaNeon)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Vibración Háptica", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = hapticFeedbackEnabled,
                            onCheckedChange = { hapticFeedbackEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = mambaNeon)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = Color.LightGray.copy(alpha = 0.2f))

                    // BORRAR CACHÉ (Acción)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* Acción de borrar caché */ }
                            .padding(horizontal = 20.dp, vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Borrar caché de la app", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.weight(1f))
                        Text("124 MB", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}
