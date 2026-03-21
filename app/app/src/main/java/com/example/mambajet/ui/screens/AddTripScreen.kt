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
import com.example.mambajet.domain.Trip // IMPORTANTE: Importar tu modelo de dominio
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTripScreen(
    onBack: () -> Unit,
    onTripAdded: (Trip) -> Unit // NUEVO: Callback para enviar el viaje al ViewModel
) {
    val mambaNeon = Color(0xFF2DB300)

    // --- ESTADOS DEL FORMULARIO ---
    var tripName by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var estimatedBudget by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") } // NUEVO: Requerido por la Tarea 1.1

    // Control de Diálogos
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

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

            OutlinedTextField(
                value = tripName,
                onValueChange = { tripName = it },
                label = { Text("Nombre del viaje") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Edit, null, tint = mambaNeon) }
            )

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
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        filteredOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = { destination = selectionOption; expanded = false }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = estimatedBudget,
                onValueChange = { estimatedBudget = it },
                label = { Text("Presupuesto Estimado (€)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, null, tint = mambaNeon) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // NUEVO: Campo de descripción requerido por la rúbrica
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción del viaje") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Description, null, tint = mambaNeon) }
            )

            ClickableDateField(
                label = "Fecha de Inicio",
                value = startDate,
                icon = Icons.Default.DateRange,
                color = mambaNeon,
                onClick = { showStartDatePicker = true }
            )

            ClickableDateField(
                label = "Fecha Final",
                value = endDate,
                icon = Icons.Default.FlightLand,
                color = mambaNeon,
                onClick = { showEndDatePicker = true }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // CREAMOS EL OBJETO REAL Y LO ENVIAMOS AL VIEWMODEL
                    val newTrip = Trip(
                        title = if (tripName.isNotEmpty()) tripName else destination,
                        startDate = startDate,
                        endDate = endDate,
                        description = description,
                        totalBudget = estimatedBudget.toDoubleOrNull() ?: 0.0,
                        spentBudget = 0.0
                    )
                    onTripAdded(newTrip)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = mambaNeon),
                enabled = tripName.isNotEmpty() && destination.isNotEmpty() &&
                        startDate.isNotEmpty() && endDate.isNotEmpty() && estimatedBudget.isNotEmpty() && description.isNotEmpty()
            ) {
                Text("CONFIRMAR VIAJE", fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }
        }
    }

    if (showStartDatePicker) MambaDatePicker(onDateSelected = { startDate = it }, onDismiss = { showStartDatePicker = false })
    if (showEndDatePicker) MambaDatePicker(onDateSelected = { endDate = it }, onDismiss = { showEndDatePicker = false })
}

@Composable
fun ClickableDateField(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = false,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MambaDatePicker(onDateSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val datePickerState = rememberDatePickerState()
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // IMPORTANTE: Formato dd/MM/YYYY requerido

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