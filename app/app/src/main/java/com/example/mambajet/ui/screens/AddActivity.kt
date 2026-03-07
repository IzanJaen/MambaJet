package com.example.mambajet.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// IMPORTANTE: Asegúrate de importar el PlanType de tu modelo de dominio
import com.example.mambajet.domain.PlanType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(onBack: () -> Unit) {
    val mambaNeon = Color(0xFF2DB300)

    // --- ESTADOS ---
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // NUEVO: Estado para el tipo de actividad (por defecto EXPLORATION)
    var selectedType by remember { mutableStateOf(PlanType.EXPLORATION) }

    // Control de diálogos
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // NUEVO: Mapa de iconos para la UI de selección
    val planIcons = mapOf(
        PlanType.FLIGHT to Icons.Default.Flight,
        PlanType.HOTEL to Icons.Default.Hotel,
        PlanType.RESTAURANT to Icons.Default.Restaurant,
        PlanType.TRANSPORT to Icons.Default.DirectionsCar,
        PlanType.EXPLORATION to Icons.Default.Explore
    )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("NUEVA ACTIVIDAD", letterSpacing = 4.sp, fontSize = 12.sp, fontWeight = FontWeight.Light) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.Close, null) }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Detalles del Plan", fontWeight = FontWeight.Black, fontSize = 28.sp)

            // --- NUEVO: SELECTOR DE TIPO DE ACTIVIDAD ---
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Categoría", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(PlanType.values()) { type ->
                        val isSelected = selectedType == type
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) mambaNeon else Color(0xFFF8F9FA), // Gris claro si no está seleccionado
                            modifier = Modifier.clickable { selectedType = type }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = planIcons[type] ?: Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else Color.DarkGray,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = type.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }
            // ---------------------------------------------

            // 1. Título
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título (ej. Cena en Shibuya)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Event, null, tint = mambaNeon) }
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // 2. Fecha (Mitad de la pantalla)
                Box(modifier = Modifier.weight(1f)) {
                    ClickableDateField(
                        label = "Fecha",
                        value = date,
                        icon = Icons.Default.DateRange,
                        color = mambaNeon,
                        onClick = { showDatePicker = true }
                    )
                }

                // 3. Hora (Mitad de la pantalla)
                Box(modifier = Modifier.weight(1f)) {
                    ClickableDateField(
                        label = "Hora",
                        value = time,
                        icon = Icons.Default.AccessTime,
                        color = mambaNeon,
                        onClick = { showTimePicker = true }
                    )
                }
            }

            // 4. Precio
            OutlinedTextField(
                value = cost,
                onValueChange = { cost = it },
                label = { Text("Coste estimado (€)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.AttachMoney, null, tint = mambaNeon) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Solo teclado numérico
            )

            // 5. Notas
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Notas o localizador") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Description, null, tint = mambaNeon) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onBack() }, // @TODO: Guardar selectedType junto con el resto de datos
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = mambaNeon),
                enabled = title.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty() && cost.isNotEmpty()
            ) {
                Text("AÑADIR AL ITINERARIO", fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }
        }
    }

    // --- DIÁLOGOS ---
    if (showDatePicker) {
        MambaDatePicker(
            onDateSelected = { date = it },
            onDismiss = { showDatePicker = false }
        )
    }

    if (showTimePicker) {
        MambaTimePicker(
            onTimeSelected = { time = it },
            onDismiss = { showTimePicker = false }
        )
    }
}

// Selector de Hora Nativo
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MambaTimePicker(onTimeSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val state = rememberTimePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onTimeSelected("${state.hour}:${state.minute.toString().padStart(2, '0')}")
                onDismiss()
            }) { Text("SELECCIONAR", color = Color(0xFF2DB300)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("CANCELAR") }
        },
        text = {
            TimePicker(state = state)
        }
    )
}
