package com.example.mambajet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.mambajet.R

data class Trip(val destination: String, val date: String, val totalBudget: Double, val spentBudget: Double, val icon: ImageVector)

val mockTrips = listOf(
    Trip("Tokio, Japón", "15 May - 20 May", 2500.0, 1200.0, Icons.Default.LocationCity),
    Trip("París, Francia", "02 Jun - 05 Jun", 1200.0, 450.0, Icons.Default.AccountBalance),
    Trip("Nueva York, USA", "10 Dic - 15 Dic", 3000.0, 150.0, Icons.Default.Business)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// --- AQUÍ AÑADIMOS onTermsClick ---
fun HomeScreen(onTripClick: (String) -> Unit, onAddTripClick: () -> Unit, onGalleryClick: () -> Unit, onTermsClick: () -> Unit, onAboutClick: () -> Unit) {
    val mambaNeon = Color(0xFF2DB300)
    var showProfileSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.mambajet_banner),
                        contentDescription = "MambaJet Logo",
                        modifier = Modifier.height(40.dp),
                        contentScale = ContentScale.Fit
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
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
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        HomeBottomActionIcon(icon = Icons.Default.PhotoLibrary, label = "Galería") { onGalleryClick() }
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        FloatingActionButton(
                            onClick = onAddTripClick,
                            containerColor = mambaNeon,
                            contentColor = Color.White,
                            shape = RoundedCornerShape(16.dp),
                            elevation = FloatingActionButtonDefaults.elevation(0.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo Viaje", modifier = Modifier.size(28.dp))
                        }
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        HomeBottomActionIcon(icon = Icons.Default.Person, label = "Perfil") { showProfileSheet = true }
                    }
                }
            }
        }
    ) { paddingValues ->
        MainContent(paddingValues, onTripClick)

        if (showProfileSheet) {
            ModalBottomSheet(
                onDismissRequest = { showProfileSheet = false },
                containerColor = Color.White,
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.LightGray) }
            ) {
                // Pasamos la acción al componente
                ProfileSheetContent(
                    color = mambaNeon,
                    onTermsClick = {
                        showProfileSheet = false
                        onTermsClick()
                    },
                    onAboutClick = {
                        showProfileSheet = false
                        onAboutClick()
                    }
                )
            }
        }
    }
}
@Composable
fun MainContent(paddingValues: PaddingValues, onTripClick: (String) -> Unit) {
    val mambaNeon = Color(0xFF2DB300)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color.White)
    ) {
        Text(
            text = "Tus Próximos Viajes",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            itemsIndexed(mockTrips) { index, viaje ->
                val isFirst = index == 0
                val progress = if (viaje.totalBudget > 0) (viaje.spentBudget / viaje.totalBudget).toFloat() else 0f

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(if (isFirst) 140.dp else 120.dp)
                        .clickable { onTripClick(viaje.destination) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFirst) Color(0xFFE8F5E9) else Color(0xFFF8F9FA)
                    ),
                    elevation = CardDefaults.cardElevation(if (isFirst) 4.dp else 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = viaje.destination,
                                    fontWeight = FontWeight.Bold,
                                    style = if (isFirst) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = viaje.date,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isFirst) Color(0xFF2E7D32) else Color.DarkGray
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = if (isFirst) Color.White else Color.LightGray.copy(alpha = 0.2f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = viaje.icon,
                                    contentDescription = null,
                                    tint = if (isFirst) mambaNeon else Color.Gray,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(if (isFirst) 12.dp else 8.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Presupuesto: ${(progress * 100).toInt()}%",
                                fontSize = if (isFirst) 14.sp else 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isFirst) mambaNeon else Color.Gray
                            )
                            Text(
                                "${viaje.spentBudget.toInt()}€ / ${viaje.totalBudget.toInt()}€",
                                fontSize = if (isFirst) 14.sp else 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isFirst) Color.Black else Color.DarkGray
                            )
                        }
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(if (isFirst) 8.dp else 4.dp).clip(CircleShape),
                            color = mambaNeon,
                            trackColor = if (isFirst) Color.White else Color.LightGray.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeBottomActionIcon(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(icon, contentDescription = label, tint = Color.Gray, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}


@Composable
fun ProfileSheetContent(color: Color, onTermsClick: () -> Unit, onAboutClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp).padding(bottom = 32.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(64.dp), tint = color)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "Izan Jaén", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Text(text = "MAMBA ELITE MEMBER", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color, letterSpacing = 1.sp)
            }
        }

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(bottom = 16.dp))

        ProfileMenuItem(icon = Icons.Default.Settings, text = "Ajustes de Usuario", color = color) {}

        // --- AQUÍ CONECTAMOS EL CLICK DE ABOUT US ---
        ProfileMenuItem(icon = Icons.Default.People, text = "Sobre Nosotros", color = color, onClick = onAboutClick)

        ProfileMenuItem(icon = Icons.Default.AssignmentTurnedIn, text = "Términos y Condiciones", color = color, onClick = onTermsClick)
        ProfileMenuItem(icon = Icons.Default.Build, text = "Ajustes de App", color = color) {}
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, text: String, color: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // Añadimos acción dinámica
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontWeight = FontWeight.Medium, fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}