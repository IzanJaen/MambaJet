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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mambajet.R
import com.example.mambajet.ui.viewmodels.SettingsViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsScreen(viewModel: SettingsViewModel, onBack: () -> Unit, onLogout: () -> Unit) {
    val mambaNeon = Color(0xFF2DB300)
    val dangerRed = Color(0xFFFF3B30)

    val currentUsername by viewModel.username.collectAsState()
    val currentDob by viewModel.dateOfBirth.collectAsState()
    val currentEmail by viewModel.email.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.profile), letterSpacing = 4.sp, fontWeight = FontWeight.Light, fontSize = 12.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBackIosNew, null, modifier = Modifier.size(20.dp)) } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background, titleContentColor = MaterialTheme.colorScheme.onBackground, navigationIconContentColor = MaterialTheme.colorScheme.onBackground)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(90.dp).clip(CircleShape).background(mambaNeon.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, null, modifier = Modifier.size(50.dp), tint = mambaNeon) }
                Spacer(modifier = Modifier.height(12.dp))
                Text(stringResource(R.string.mamba_elite), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = mambaNeon, letterSpacing = 1.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(stringResource(R.string.profile_settings), fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 8.dp, start = 8.dp))

            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(0.dp)) {
                Column {
                    ProfileFieldItem(stringResource(R.string.name), currentUsername, mambaNeon) { tempName = currentUsername; showEditNameDialog = true }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = Color.LightGray.copy(alpha = 0.2f))

                    ProfileFieldItem(stringResource(R.string.dob), currentDob, mambaNeon) { showDatePicker = true }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = Color.LightGray.copy(alpha = 0.2f))

                    ProfileFieldItem(stringResource(R.string.email), currentEmail.ifBlank { "—" }, mambaNeon) { }                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { showLogoutDialog = true }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = dangerRed, contentColor = Color.White)) {
                Icon(Icons.Default.ExitToApp, null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.logout), fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        if (showEditNameDialog) {
            AlertDialog(
                onDismissRequest = { showEditNameDialog = false },
                title = { Text(stringResource(R.string.edit), color = MaterialTheme.colorScheme.onSurface) },
                text = { OutlinedTextField(value = tempName, onValueChange = { tempName = it }, label = { Text(stringResource(R.string.name)) }, modifier = Modifier.fillMaxWidth(), singleLine = true) },
                confirmButton = { TextButton(onClick = { viewModel.updateUsername(tempName); showEditNameDialog = false }) { Text(stringResource(R.string.save), color = mambaNeon, fontWeight = FontWeight.Bold) } },
                dismissButton = { TextButton(onClick = { showEditNameDialog = false }) { Text(stringResource(R.string.cancel), color = Color.Gray) } },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                            viewModel.updateDateOfBirth(sdf.format(java.util.Date(millis)))
                        }
                        showDatePicker = false
                    }) { Text("OK", color = mambaNeon, fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(stringResource(R.string.cancel), color = Color.Gray)
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text(stringResource(R.string.logout_title), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
                text = { Text(stringResource(R.string.logout_msg), color = MaterialTheme.colorScheme.onSurface) },
                confirmButton = { TextButton(onClick = { showLogoutDialog = false; onLogout() }) { Text(stringResource(R.string.exit), color = dangerRed, fontWeight = FontWeight.Bold) } },
                dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text(stringResource(R.string.cancel), color = Color.Gray) } },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
fun ProfileFieldItem(label: String, value: String, accentColor: Color, onEditClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onEditClick() }.padding(horizontal = 20.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
        }
        Text(text = stringResource(R.string.edit), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = accentColor)
    }
}