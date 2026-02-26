package com.example.mambajet.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(onBack: () -> Unit) {
    val mambaNeon = Color(0xFF2DB300)

    // --- ESTADOS ---
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") } // NUEVO: Fecha
    var time by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") } // NUEVO: Coste
    var description by remember { mutableStateOf("") }

    // Control de diálogos
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

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
                    ClickableDateField( // Reutilizamos tu función ClickableDateField para la hora también
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
                onClick = { onBack() },
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
        MambaDatePicker( // Reutiliza el de AddTripScreen
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
