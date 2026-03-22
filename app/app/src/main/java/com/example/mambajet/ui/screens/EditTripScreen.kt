package com.example.mambajet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mambajet.R
import com.example.mambajet.domain.Trip
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTripScreen(tripToEdit: Trip, onBack: () -> Unit, onTripUpdated: (Trip) -> Unit) {
    val mambaNeon = Color(0xFF2DB300)
    var tripName by remember { mutableStateOf(tripToEdit.title) }
    var estimatedBudget by remember { mutableStateOf(tripToEdit.totalBudget.toInt().toString()) }
    var startDate by remember { mutableStateOf(tripToEdit.startDate) }
    var endDate by remember { mutableStateOf(tripToEdit.endDate) }
    var description by remember { mutableStateOf(tripToEdit.description) }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf<String?>(null) }

    val errorEndDate = stringResource(R.string.error_end_date)

    LaunchedEffect(startDate, endDate) {
        val start = parseDateValidator(startDate)
        val end = parseDateValidator(endDate)
        if (start != null && end != null && end.before(start)) {
            dateError = errorEndDate
        } else { dateError = null }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.edit_trip_title), letterSpacing = 4.sp, fontSize = 12.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.Close, null) } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background, titleContentColor = MaterialTheme.colorScheme.onBackground, navigationIconContentColor = MaterialTheme.colorScheme.onBackground)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(stringResource(R.string.trip_settings), fontWeight = FontWeight.Black, fontSize = 28.sp, color = MaterialTheme.colorScheme.onBackground)

            OutlinedTextField(value = tripName, onValueChange = { tripName = it }, label = { Text(stringResource(R.string.trip_name)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), leadingIcon = { Icon(Icons.Default.Edit, null, tint = mambaNeon) })
            OutlinedTextField(value = estimatedBudget, onValueChange = { estimatedBudget = it }, label = { Text(stringResource(R.string.est_budget)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, null, tint = mambaNeon) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.trip_desc)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), leadingIcon = { Icon(Icons.Default.Description, null, tint = mambaNeon) })

            ClickableDateField(stringResource(R.string.start_date), startDate, Icons.Default.DateRange, mambaNeon) { showStartDatePicker = true }
            ClickableDateField(stringResource(R.string.end_date), endDate, Icons.Default.FlightLand, mambaNeon) { showEndDatePicker = true }

            Spacer(modifier = Modifier.weight(1f))

            if (dateError != null) Text(text = "⚠️ $dateError", color = Color(0xFFFF3B30), fontSize = 12.sp, fontWeight = FontWeight.Bold)

            Button(
                onClick = { onTripUpdated(Trip(id = tripToEdit.id, userId = tripToEdit.userId, title = tripName, startDate = startDate, endDate = endDate, description = description, totalBudget = estimatedBudget.toDoubleOrNull() ?: tripToEdit.totalBudget, spentBudget = tripToEdit.spentBudget)); onBack() },
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = mambaNeon), enabled = tripName.isNotEmpty() && dateError == null
            ) { Text(stringResource(R.string.save_changes), fontWeight = FontWeight.Bold, letterSpacing = 2.sp) }
        }
    }
    if (showStartDatePicker) MambaDatePicker(onDateSelected = { startDate = it }, onDismiss = { showStartDatePicker = false })
    if (showEndDatePicker) MambaDatePicker(onDateSelected = { endDate = it }, onDismiss = { showEndDatePicker = false })
}