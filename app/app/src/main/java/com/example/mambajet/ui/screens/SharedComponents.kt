package com.example.mambajet.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ClickableDateField(label: String, value: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
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
                disabledTextColor = MaterialTheme.colorScheme.onBackground,
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
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { onDateSelected(formatter.format(Date(it))) }
                onDismiss()
            }) { Text("SELECCIONAR", color = Color(0xFF2DB300)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("CANCELAR", color = Color.Gray) } }
        // Se ha eliminado el containerColor para que use el del sistema por defecto
    ) {
        DatePicker(state = datePickerState)
    }
}

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
            TextButton(onClick = onDismiss) { Text("CANCELAR", color = Color.Gray) }
        },
        text = { TimePicker(state = state) }
        // Se ha eliminado el containerColor para que use el del sistema por defecto
    )
}

fun parseDateValidator(dateString: String): Date? {
    if (dateString.isEmpty()) return null
    return try { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString) } catch (e: Exception) { null }
}