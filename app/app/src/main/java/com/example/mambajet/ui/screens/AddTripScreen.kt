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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTripScreen(onBack: () -> Unit) {
    val mambaNeon = Color(0xFF2DB300)

    // --- ESTADOS DEL FORMULARIO ---
    var tripName by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var estimatedBudget by remember { mutableStateOf("") } // NUEVO: Presupuesto
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    // Control de Diálogos
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // Simulación lista para destinos
    val citySuggestions = listOf("Tokio, Japón", "París, Francia", "Nueva York, USA", "Madrid, España")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("NUEVO VIAJE", letterSpacing = 4.sp, fontSize = 12.sp) },
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
            Text("Inicia tu Viaje", fontWeight = FontWeight.Black, fontSize = 28.sp)

            // 1. Campo para el Nombre
            OutlinedTextField(
                value = tripName,
                onValueChange = { tripName = it },
                label = { Text("Nombre del viaje") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Edit, null, tint = mambaNeon) }
            )

            // 2. Campo para el Destino
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = destination,
                    onValueChange = { destination = it; expanded = it.isNotEmpty() },
                    label = { Text("Destino") },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Place, null, tint = mambaNeon) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )

                val filteredOptions = citySuggestions.filter { it.contains(destination, ignoreCase = true) }
                if (filteredOptions.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        filteredOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = { destination = selectionOption; expanded = false }
                            )
                        }
                    }
                }
            }

            // --- NUEVO: 3. Campo Presupuesto Estimado ---
            OutlinedTextField(
                value = estimatedBudget,
                onValueChange = { estimatedBudget = it },
                label = { Text("Presupuesto Estimado (€)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, null, tint = mambaNeon) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Solo números
            )

            // 4. Campo Fecha Inicio
            ClickableDateField(
                label = "Fecha de Inicio",
                value = startDate,
                icon = Icons.Default.DateRange,
                color = mambaNeon,
                onClick = { showStartDatePicker = true }
            )

            // 5. Campo Fecha Final
            ClickableDateField(
                label = "Fecha Final",
                value = endDate,
                icon = Icons.Default.FlightLand,
                color = mambaNeon,
                onClick = { showEndDatePicker = true }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onBack() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = mambaNeon),
                enabled = tripName.isNotEmpty() && destination.isNotEmpty() &&
                        startDate.isNotEmpty() && endDate.isNotEmpty() && estimatedBudget.isNotEmpty()
            ) {
                Text("CONFIRMAR VIAJE", fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }
        }
    }

    // --- DIÁLOGOS DE CALENDARIO ---
    if (showStartDatePicker) {
        MambaDatePicker(
            onDateSelected = { startDate = it },
            onDismiss = { showStartDatePicker = false }
        )
    }

    if (showEndDatePicker) {
        MambaDatePicker(
            onDateSelected = { endDate = it },
            onDismiss = { showEndDatePicker = false }
        )
    }
}

// Para que el calendario salga dándole a todo el click no solo al icono
@Composable
fun ClickableDateField(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = false, // Esto permite que el clic lo capture el Box
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(icon, null, tint = color) },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledBorderColor = Color.Gray,
                disabledLabelColor = Color.Gray,
                disabledLeadingIconColor = color
            )
        )
    }
}

// Calendario
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MambaDatePicker(onDateSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val datePickerState = rememberDatePickerState()
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    onDateSelected(formatter.format(Date(it)))
                }
                onDismiss()
            }) { Text("SELECCIONAR", color = Color(0xFF2DB300)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("CANCELAR") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}