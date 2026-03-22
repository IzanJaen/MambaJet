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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mambajet.R
import com.example.mambajet.domain.Activity
import com.example.mambajet.domain.PlanType
import com.example.mambajet.ui.viewmodels.ActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditActivityScreen(activityId: String, tripStartDate: String, tripEndDate: String, viewModel: ActivityViewModel, onBack: () -> Unit, onActivityUpdated: (Activity) -> Unit) {
    val mambaNeon = Color(0xFF2DB300)
    val activities by viewModel.activities.collectAsState()
    val activityToEdit = activities.find { it.id == activityId }

    if (activityToEdit == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = mambaNeon) }
        return
    }

    var title by remember { mutableStateOf(activityToEdit.title) }
    var date by remember { mutableStateOf(activityToEdit.date) }
    var time by remember { mutableStateOf(activityToEdit.time) }
    var cost by remember { mutableStateOf(activityToEdit.cost.toInt().toString()) }
    var description by remember { mutableStateOf(activityToEdit.description) }
    var selectedType by remember { mutableStateOf(activityToEdit.type) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf<String?>(null) }

    val errorBounds = stringResource(R.string.error_date_bounds)

    LaunchedEffect(date) {
        val actDate = parseDateValidator(date)
        val tStart = parseDateValidator(tripStartDate)
        val tEnd = parseDateValidator(tripEndDate)
        if (actDate != null && tStart != null && tEnd != null) {
            dateError = if (actDate.before(tStart) || actDate.after(tEnd)) errorBounds else null
        } else { dateError = null }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.edit_plan), letterSpacing = 4.sp, fontSize = 12.sp, fontWeight = FontWeight.Light) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.Close, null) } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background, titleContentColor = MaterialTheme.colorScheme.onBackground, navigationIconContentColor = MaterialTheme.colorScheme.onBackground)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(stringResource(R.string.plan_details), fontWeight = FontWeight.Black, fontSize = 28.sp, color = MaterialTheme.colorScheme.onBackground)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.category), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(PlanType.values()) { type ->
                        val isSelected = selectedType == type
                        Surface(shape = RoundedCornerShape(12.dp), color = if (isSelected) mambaNeon else MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.clickable { selectedType = type }) {
                            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = type.getIcon(), contentDescription = null, tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = type.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }

            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text(stringResource(R.string.title_ph)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), leadingIcon = { Icon(Icons.Default.Event, null, tint = mambaNeon) })

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(modifier = Modifier.weight(1f)) { ClickableDateField(stringResource(R.string.date), date, Icons.Default.DateRange, mambaNeon) { showDatePicker = true } }
                Box(modifier = Modifier.weight(1f)) { ClickableDateField(stringResource(R.string.time), time, Icons.Default.AccessTime, mambaNeon) { showTimePicker = true } }
            }

            OutlinedTextField(value = cost, onValueChange = { cost = it }, label = { Text(stringResource(R.string.cost)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), leadingIcon = { Icon(Icons.Default.AttachMoney, null, tint = mambaNeon) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.notes)) }, modifier = Modifier.fillMaxWidth().height(120.dp), shape = RoundedCornerShape(12.dp), leadingIcon = { Icon(Icons.Default.Description, null, tint = mambaNeon) })

            Spacer(modifier = Modifier.weight(1f))

            if (dateError != null) Text(text = "⚠️ $dateError", color = Color(0xFFFF3B30), fontSize = 12.sp, fontWeight = FontWeight.Bold)

            Button(
                onClick = { onActivityUpdated(Activity(id = activityToEdit.id, tripId = activityToEdit.tripId, title = title, description = description, date = date, time = time, cost = cost.toDoubleOrNull() ?: 0.0, type = selectedType)); onBack() },
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = mambaNeon), enabled = title.isNotEmpty() && dateError == null
            ) { Text(stringResource(R.string.save_changes), fontWeight = FontWeight.Bold, letterSpacing = 2.sp) }
        }
    }
    if (showDatePicker) MambaDatePicker(onDateSelected = { date = it }, onDismiss = { showDatePicker = false })
    if (showTimePicker) MambaTimePicker(onTimeSelected = { time = it }, onDismiss = { showTimePicker = false })
}

