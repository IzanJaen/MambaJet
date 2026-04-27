package com.example.mambajet.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mambajet.R
import com.example.mambajet.domain.Activity
import com.example.mambajet.domain.PlanType
import com.example.mambajet.ui.viewmodels.ActivityViewModel
import com.example.mambajet.ui.viewmodels.TripListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(destination: String, tripId: String,  viewModel: ActivityViewModel, tripViewModel: TripListViewModel, onBack: () -> Unit, onAddActivityClick: () -> Unit, onGalleryClick: () -> Unit, onMapClick: () -> Unit, onAIClick: () -> Unit, onDeleteConfirm: () -> Unit, onEditTripClick: () -> Unit, onEditActivityClick: (String) -> Unit) {
    val mambaNeon = Color(0xFF2DB300)
    val dangerRed = Color(0xFFFF3B30)
    val aiAccent = Color(0xFFAF4B7C)

    var showMenu by remember { mutableStateOf(false) }
    var showDeleteTripDialog by remember { mutableStateOf(false) }
    var activityToDelete by remember { mutableStateOf<Activity?>(null) }

    LaunchedEffect(tripId) { viewModel.loadActivities(tripId) }
    val activities by viewModel.activities.collectAsState()
    val groupedActivities = activities.groupBy { it.date }
    LaunchedEffect(activities) {
        val totalCost = activities.sumOf { it.cost }
        tripViewModel.updateTripBudget(tripId, totalCost)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.trip_plan), letterSpacing = 4.sp, fontWeight = FontWeight.Light, fontSize = 12.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBackIosNew, null, modifier = Modifier.size(20.dp)) } },
                actions = {
                    IconButton(onClick = onAIClick) { Icon(Icons.Default.AutoAwesome, null, tint = aiAccent) }
                    IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, stringResource(R.string.options)) }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }, modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                        DropdownMenuItem(text = { Text(stringResource(R.string.trip_settings), color = MaterialTheme.colorScheme.onSurface) }, onClick = { showMenu = false; onEditTripClick() }, leadingIcon = { Icon(Icons.Default.Settings, null, tint = Color.Gray) })
                        DropdownMenuItem(text = { Text(stringResource(R.string.delete_mission), color = dangerRed, fontWeight = FontWeight.Bold) }, onClick = { showMenu = false; showDeleteTripDialog = true }, leadingIcon = { Icon(Icons.Default.DeleteForever, null, tint = dangerRed) })
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background, titleContentColor = MaterialTheme.colorScheme.onBackground, navigationIconContentColor = MaterialTheme.colorScheme.onBackground)
            )
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 16.dp, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)) {
                Row(modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) { BottomActionIcon(Icons.Default.PhotoLibrary, stringResource(R.string.gallery)) { onGalleryClick() } }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        FloatingActionButton(onClick = onAddActivityClick, containerColor = mambaNeon, contentColor = Color.White, shape = RoundedCornerShape(16.dp), elevation = FloatingActionButtonDefaults.elevation(0.dp)) {
                            Icon(Icons.Default.Add, stringResource(R.string.add), modifier = Modifier.size(28.dp))
                        }
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) { BottomActionIcon(Icons.Default.Map, stringResource(R.string.map)) { onMapClick() } }
                }
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp)) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)) {
                    Column {
                        Text(destination.uppercase(), fontSize = 40.sp, fontWeight = FontWeight.Black, letterSpacing = (-2).sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                        Text(destination, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
            item {
                val totalCost = activities.sumOf { it.cost }
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), colors = CardDefaults.cardColors(containerColor = mambaNeon), shape = RoundedCornerShape(20.dp)) {
                    Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(stringResource(R.string.current_expense), color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text("${totalCost.toInt()}€", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(stringResource(R.string.activities), color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text("${activities.size}", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
            groupedActivities.forEach { (date, dailyActivities) ->
                item { Text(date.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier.padding(bottom = 12.dp, top = 16.dp)) }
                items(dailyActivities, key = { it.id }) { activity ->
                    val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = { value -> if (value == SwipeToDismissBoxValue.EndToStart) { activityToDelete = activity; false } else false })
                    FuturisticTimelineItem(activity, mambaNeon, dismissState, onClick = { onEditActivityClick(activity.id) })
                }
            }
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }

    if (activityToDelete != null) {
        AlertDialog(
            onDismissRequest = { activityToDelete = null },
            title = { Text(stringResource(R.string.delete_plan), color = MaterialTheme.colorScheme.onSurface) },
            text = { Text(stringResource(R.string.delete_plan_msg), color = MaterialTheme.colorScheme.onSurface) },
            confirmButton = { TextButton(onClick = { viewModel.deleteActivity(activityToDelete!!.id, destination); activityToDelete = null }) { Text(stringResource(R.string.delete), color = dangerRed, fontWeight = FontWeight.Bold) } },
            dismissButton = { TextButton(onClick = { activityToDelete = null }) { Text(stringResource(R.string.cancel), color = Color.Gray) } },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    if (showDeleteTripDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteTripDialog = false },
            title = { Text(stringResource(R.string.destroy_mission), color = MaterialTheme.colorScheme.onSurface) },
            text = { Text(stringResource(R.string.destroy_mission_msg), color = MaterialTheme.colorScheme.onSurface) },
            confirmButton = { TextButton(onClick = { showDeleteTripDialog = false; onDeleteConfirm() }) { Text(stringResource(R.string.destroy), color = dangerRed, fontWeight = FontWeight.Bold) } },
            dismissButton = { TextButton(onClick = { showDeleteTripDialog = false }) { Text(stringResource(R.string.cancel), color = Color.Gray) } },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuturisticTimelineItem(activity: Activity, color: Color, dismissState: SwipeToDismissBoxState, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = activity.time, modifier = Modifier.width(45.dp).padding(top = 16.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onBackground)
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 16.dp).padding(top = 20.dp)) {
            Box(modifier = Modifier.size(8.dp).background(color, CircleShape).border(2.dp, MaterialTheme.colorScheme.background, CircleShape))
            Box(modifier = Modifier.width(2.dp).height(80.dp).background(Brush.verticalGradient(listOf(color, Color.Transparent))))
        }
        SwipeToDismissBox(state = dismissState, enableDismissFromStartToEnd = false, modifier = Modifier.fillMaxWidth(), backgroundContent = { Box(modifier = Modifier.fillMaxSize().padding(bottom = 16.dp).clip(RoundedCornerShape(topStart = 0.dp, bottomStart = 24.dp, topEnd = 24.dp, bottomEnd = 24.dp)).background(Color(0xFFFF3B30)), contentAlignment = Alignment.CenterEnd) { Icon(Icons.Default.Delete, null, tint = Color.White, modifier = Modifier.padding(end = 24.dp)) } }) {
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).clickable { onClick() }, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 24.dp, topEnd = 24.dp, bottomEnd = 24.dp)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(modifier = Modifier.size(40.dp), color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(12.dp), shadowElevation = 2.dp) { Icon(activity.type.getIcon(), null, tint = color, modifier = Modifier.padding(10.dp)) }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(activity.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                        if (activity.description.isNotEmpty()) Text(activity.description, fontSize = 12.sp, color = Color.Gray)
                    }
                    Text("${activity.cost.toInt()}€", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                }
            }
        }
    }
}
@Composable
fun BottomActionIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }.padding(8.dp)) {
        Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}