package com.izanjaen.mambajet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed // Cambiado para identificar el primero
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

import com.example.mambajet.R

data class Trip(val destination: String, val date: String)

val mockTrips = listOf(
    Trip("Tokio, Japón", "15 May - 20 May"),
    Trip("París, Francia", "02 Jun - 05 Jun"),
    Trip("Nueva York, USA", "10 Dic - 15 Dic")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onTripClick: (String) -> Unit, onAddTripClick: () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { ModalDrawerSheet { DrawerContent() } }
    ) {
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
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            },
            floatingActionButton = {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text("Nuevo viaje")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.primary,
                        onClick = onAddTripClick
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo", tint = Color.White)
                    }
                }
            }
        ) { paddingValues ->
            MainContent(paddingValues, onTripClick)
        }
    }
}

@Composable
fun MainContent(paddingValues: PaddingValues, onTripClick: (String) -> Unit) {
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

        LazyColumn {
            // Cambiamos a itemsIndexed para detectar el primer elemento
            itemsIndexed(mockTrips) { index, viaje ->
                val isFirst = index == 0

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        // Si es el primero, lo hacemos un poco más alto
                        .height(if (isFirst) 130.dp else 90.dp)
                        .clickable { onTripClick(viaje.destination) },
                    colors = CardDefaults.cardColors(
                        // Si es el primero, usamos un verde muy ligero (E8F5E9)
                        containerColor = if (isFirst) Color(0xFFE8F5E9) else Color(0xFFF1F1F1)
                    ),
                    // Añadimos un poco más de elevación al primero para que resalte
                    elevation = CardDefaults.cardElevation(if (isFirst) 4.dp else 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = viaje.destination,
                            fontWeight = FontWeight.Bold,
                            // Si es el primero, el texto es un poco más grande
                            style = if (isFirst) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = viaje.date,
                            style = MaterialTheme.typography.bodyMedium,
                            // Si es el primero, le damos un tono verde oscuro al texto de la fecha
                            color = if (isFirst) Color(0xFF2E7D32) else Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerContent() {
    Column(modifier = Modifier.fillMaxHeight().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "Izan Jaén", style = MaterialTheme.typography.titleMedium)
                Text(text = "Viajero Premium", style = MaterialTheme.typography.bodySmall)
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))
        NavigationDrawerItem(
            label = { Text("Ajustes de Usuario") },
            selected = false,
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            onClick = { }
        )
        NavigationDrawerItem(
            label = { Text("Sobre Nosotros") },
            selected = false,
            icon = { Icon(Icons.Default.People, contentDescription = null) },
            onClick = { }
        )
        NavigationDrawerItem(
            label = { Text("Términos y Condiciones") },
            selected = false,
            icon = { Icon(Icons.Default.AssignmentTurnedIn, contentDescription = null) },
            onClick = { }
        )
        NavigationDrawerItem(
            label = { Text("Ajustes de App") },
            selected = false,
            icon = { Icon(Icons.Default.Build, contentDescription = null) },
            onClick = { }
        )
    }
}