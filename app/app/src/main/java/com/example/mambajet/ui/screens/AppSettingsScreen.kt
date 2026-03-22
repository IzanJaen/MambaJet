package com.example.mambajet.ui.screens

import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mambajet.R
import com.example.mambajet.ui.viewmodels.SettingsViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val mambaNeon = Color(0xFF2DB300)
    val context = LocalContext.current as Activity // Para reiniciar la pantalla al cambiar de idioma

    val isDarkTheme by viewModel.isDarkMode.collectAsState()
    val selectedLanguage by viewModel.language.collectAsState()

    var notificationsEnabled by remember { mutableStateOf(true) }
    var hapticFeedbackEnabled by remember { mutableStateOf(true) }
    var languageExpanded by remember { mutableStateOf(false) }
    val languages = listOf("Català", "Castellano", "English")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                // USAMOS STRINGRESOURCE PARA TRADUCIR EL TÍTULO
                title = { Text(stringResource(R.string.settings_title), letterSpacing = 4.sp, fontWeight = FontWeight.Light, fontSize = 12.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBackIosNew, null, modifier = Modifier.size(20.dp)) } },
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

            Text(stringResource(R.string.settings_preferences), fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 8.dp, start = 8.dp))

            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(0.dp)) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode, contentDescription = null, tint = mambaNeon)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(R.string.settings_dark_mode), fontWeight = FontWeight.Medium, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(checked = isDarkTheme, onCheckedChange = { viewModel.updateDarkMode(it) }, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = mambaNeon))
                    }

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = Color.LightGray.copy(alpha = 0.2f))

                    ExposedDropdownMenuBox(expanded = languageExpanded, onExpandedChange = { languageExpanded = !languageExpanded }) {
                        Row(modifier = Modifier.fillMaxWidth().clickable { languageExpanded = true }.padding(horizontal = 20.dp, vertical = 20.dp).menuAnchor(), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = mambaNeon)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(stringResource(R.string.settings_language), fontWeight = FontWeight.Medium, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(selectedLanguage, fontSize = 14.sp, color = Color.Gray)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                        }

                        ExposedDropdownMenu(expanded = languageExpanded, onDismissRequest = { languageExpanded = false }) {
                            languages.forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang) },
                                    onClick = {
                                        viewModel.updateLanguage(lang)
                                        languageExpanded = false

                                        // APLICAMOS EL IDIOMA INSTANTÁNEAMENTE Y REINICIAMOS LA APP
                                        val localeCode = when (lang) {
                                            "English" -> "en"
                                            "Català" -> "ca"
                                            else -> "es"
                                        }
                                        val locale = Locale(localeCode)
                                        Locale.setDefault(locale)
                                        val config = context.resources.configuration
                                        config.setLocale(locale)
                                        context.resources.updateConfiguration(config, context.resources.displayMetrics)
                                        context.recreate() // Recarga la pantalla
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(stringResource(R.string.settings_system), fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 8.dp, start = 8.dp))

            // ... (Aquí mantienes el resto de tu código de Notificaciones y Caché igual que antes)
        }
    }
}
