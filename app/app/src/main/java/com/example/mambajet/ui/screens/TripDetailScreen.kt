package com.example.mambajet.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mambajet.R

// --- DATOS ---
data class ItineraryActivity(
    val time: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val cost: Double
)

data class DayGroup(
    val date: String,
    val activities: List<ItineraryActivity>
)

val mockItineraryData = listOf(
    DayGroup("viernes, 15 may", listOf(
        ItineraryActivity("10:06", "Vuelo MJ-200", "Iberia - Terminal 3", Icons.Default.Flight, 450.0),
        ItineraryActivity("15:00", "Hotel Imperial", "Check in: 15:00 JST\nTokyo, Shinjuku...", Icons.Default.Hotel, 850.0),
    )),
    DayGroup("sábado, 16 may", listOf(
        ItineraryActivity("11:00", "Mamba City Tour", "Exploración de Shibuya", Icons.Default.DirectionsWalk, 45.0)
    ))
)

// --- PANTALLA DE DETALLE ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(destination: String, onBack: () -> Unit, onAddActivityClick: () -> Unit, onGalleryClick: () -> Unit) {
    val mambaNeon = Color(0xFF2DB300)

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PLAN DE VIAJE", letterSpacing = 4.sp, fontWeight = FontWeight.Light, fontSize = 12.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        // --- BARRA INFERIOR SIMÉTRICA (Galería - Añadir - Mapa) ---
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 16.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Izquierda: Galería
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        BottomActionIcon(icon = Icons.Default.PhotoLibrary, label = "Galería") {onGalleryClick() }
                    }

                    // Centro: Añadir Actividad (Igual al de la HomeScreen)
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        FloatingActionButton(
                            onClick = onAddActivityClick,
                            containerColor = mambaNeon,
                            contentColor = Color.White,
                            shape = RoundedCornerShape(16.dp),
                            elevation = FloatingActionButtonDefaults.elevation(0.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Añadir", modifier = Modifier.size(28.dp))
                        }
                    }

                    // Derecha: Mapa
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        BottomActionIcon(icon = Icons.Default.Map, label = "Mapa") { /* Abrir Mapa */ }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            // Header Futurista
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)) {
                    Column {
                        Text(
                            text = destination.uppercase(),
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-2).sp,
                            color = Color.Black.copy(alpha = 0.05f)
                        )
                        Text(
                            text = destination,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.tokio_test),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.CenterEnd)
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.dp, mambaNeon, RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // --- TARJETA DE RESUMEN FINANCIERO (VERDE MAMBA) ---
            item {
                val totalCost = mockItineraryData.flatMap { it.activities }.sumOf { it.cost }
                val totalActivities = mockItineraryData.flatMap { it.activities }.size

                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = mambaNeon),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("GASTO ACTUAL", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text("${totalCost.toInt()}€", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("ACTIVIDADES", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text("$totalActivities", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            // Itinerario Dinámico
            mockItineraryData.forEach { dayGroup ->
                item {
                    Text(
                        text = dayGroup.date.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                items(dayGroup.activities) { activity ->
                    FuturisticTimelineItem(activity, mambaNeon)
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}

// COMPONENTE PARA LOS ICONOS DE LA BARRA INFERIOR
@Composable
fun BottomActionIcon(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }.padding(8.dp)
    ) {
        Icon(icon, contentDescription = label, tint = Color.Gray, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}

// LÍNEA DEL TIEMPO
@Composable
fun FuturisticTimelineItem(activity: ItineraryActivity, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Tiempo
        Text(
            text = activity.time,
            modifier = Modifier.width(45.dp).padding(top = 2.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace
        )

        // Línea Jet Stream
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight().padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(color, Color.Transparent)
                        )
                    )
            )
        }

        // Tarjeta de Actividad
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
            shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 24.dp, topEnd = 24.dp, bottomEnd = 24.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 2.dp
                ) {
                    Icon(
                        imageVector = activity.icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(activity.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    if (activity.description.isNotEmpty()) {
                        Text(activity.description, fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Text(
                    text = "${activity.cost.toInt()}€",
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}