package com.example.mambajet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GalleryImage(val color: Color, val icon: ImageVector, val title: String)
data class TripGallery(val tripName: String, val images: List<GalleryImage>)

val mockGalleryData = listOf(
    TripGallery("Tokio, Japón", listOf(GalleryImage(Color(0xFFFFCDD2), Icons.Default.TempleBuddhist, "Templo Senso-ji"), GalleryImage(Color(0xFFC8E6C9), Icons.Default.RamenDining, "Cena Ramen"), GalleryImage(Color(0xFFBBDEFB), Icons.Default.Train, "Shinkansen"), GalleryImage(Color(0xFFFFF9C4), Icons.Default.Nightlife, "Shibuya de noche"))),
    TripGallery("París, Francia", listOf(GalleryImage(Color(0xFFD1C4E9), Icons.Default.Museum, "Museo del Louvre"), GalleryImage(Color(0xFFFFE0B2), Icons.Default.LocalCafe, "Café Parisino"))),
    TripGallery("Nueva York, USA", listOf(GalleryImage(Color(0xFFB2EBF2), Icons.Default.DirectionsBoat, "Ferry a la Estatua"), GalleryImage(Color(0xFFF5F5F5), Icons.Default.LocalPizza, "Pizza en Brooklyn")))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(tripDestination: String? = null, onBack: () -> Unit) {
    val mambaNeon = Color(0xFF2DB300)
    var showAddPhotoSheet by remember { mutableStateOf(false) }

    val dataToShow = if (tripDestination.isNullOrBlank()) mockGalleryData else mockGalleryData.filter { it.tripName == tripDestination }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // DINÁMICO
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MEMORIES", letterSpacing = 4.sp, fontWeight = FontWeight.Light, fontSize = 12.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp)) } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddPhotoSheet = true }, containerColor = mambaNeon, contentColor = Color.White, shape = RoundedCornerShape(16.dp)) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Añadir Foto")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp)) {
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 24.dp)) {
                    Text("TU GALERÍA", fontSize = 40.sp, fontWeight = FontWeight.Black, letterSpacing = (-2).sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                    Text(if (tripDestination != null) tripDestination else "Capturas de Misión", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.offset(y = (-20).dp))
                }
            }

            items(dataToShow) { tripGallery -> TripGallerySection(tripGallery, mambaNeon) }

            if (dataToShow.isEmpty()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.NoPhotography, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Aún no hay capturas de esta misión", color = Color.Gray)
                    }
                }
            }
        }

        if (showAddPhotoSheet) {
            ModalBottomSheet(onDismissRequest = { showAddPhotoSheet = false }, containerColor = MaterialTheme.colorScheme.surface) {
                AddPhotoContent(mambaNeon = mambaNeon, initialTrip = tripDestination, onClose = { showAddPhotoSheet = false })
            }
        }
    }
}

@Composable
fun TripGallerySection(tripGallery: TripGallery, color: Color) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(tripGallery.tripName.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = color)
            Text("${tripGallery.images.size} CAPTURAS", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        }
        LazyRow(contentPadding = PaddingValues(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(tripGallery.images) { image -> GalleryImageCard(image) }
        }
    }
}

@Composable
fun GalleryImageCard(image: GalleryImage) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(140.dp).clip(RoundedCornerShape(20.dp)).background(image.color).clickable { }, contentAlignment = Alignment.Center) {
            Icon(image.icon, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.Black.copy(alpha = 0.5f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(image.title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhotoContent(mambaNeon: Color, initialTrip: String?, onClose: () -> Unit) {
    var selectedTrip by remember { mutableStateOf(initialTrip ?: "Selecciona un viaje") }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(24.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text("Añadir Nueva Captura", fontWeight = FontWeight.Black, fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurface)

        Box(modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surfaceVariant).clickable { }, contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, tint = mambaNeon, modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Toca para explorar galería", color = Color.Gray, fontSize = 12.sp)
            }
        }

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = selectedTrip, onValueChange = {}, readOnly = true, label = { Text("¿A qué misión pertenece?") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.fillMaxWidth().menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(disabledTextColor = MaterialTheme.colorScheme.onSurface, disabledBorderColor = Color.Gray, disabledLabelColor = mambaNeon)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                mockGalleryData.forEach { trip -> DropdownMenuItem(text = { Text(trip.tripName) }, onClick = { selectedTrip = trip.tripName; expanded = false }) }
            }
        }

        Button(onClick = { onClose() }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = mambaNeon), enabled = selectedTrip != "Selecciona un viaje") {
            Text("GUARDAR RECUERDO", fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        }
    }
}
