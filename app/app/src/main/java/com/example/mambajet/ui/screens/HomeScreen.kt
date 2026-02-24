package com.izanjaen.mambajet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun HomeScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent()
            }
        }
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
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White,
                        scrolledContainerColor = Color.White
                    ),
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
                        onClick = { /* Acción */ }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo", tint = Color.White)
                    }
                }
            }
        ) { paddingValues ->
            MainContent(paddingValues)
        }
    }
}

@Composable
fun MainContent(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Tus Próximos Viajes",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold
        )

        LazyColumn {
            items(mockTrips) { viaje ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = viaje.destination, fontWeight = FontWeight.Bold)
                        Text(text = viaje.date, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerContent() {
    Column(
        modifier = Modifier.fillMaxHeight().padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
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

