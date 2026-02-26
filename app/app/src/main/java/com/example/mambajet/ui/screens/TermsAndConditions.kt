package com.example.mambajet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsScreen(onBack: () -> Unit) {
    val mambaNeon = Color(0xFF2DB300)

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("LEGAL", letterSpacing = 4.sp, fontWeight = FontWeight.Light, fontSize = 12.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()) // Permite hacer scroll si el texto es muy largo
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "TÉRMINOS Y CONDICIONES",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                lineHeight = 32.sp
            )
            Text(
                text = "Última actualización: 26 de febrero de 2026",
                fontSize = 12.sp,
                color = mambaNeon,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // Secciones Legales
            TermSection(
                title = "1. Misión y Aceptación",
                body = "Al acceder al sistema MambaJet, el usuario acepta de manera irrevocable estos términos. Si no estás de acuerdo con los protocolos de vuelo y logística aquí descritos, debes abandonar la plataforma inmediatamente."
            )

            TermSection(
                title = "2. Gestión de Itinerarios",
                body = "MambaJet provee un ecosistema cerrado para la planificación de misiones de viaje. El usuario se compromete a no introducir datos falsos, ilegales o que comprometan la seguridad de las operaciones aéreas."
            )

            TermSection(
                title = "3. Protocolo de Privacidad",
                body = "La confidencialidad es nuestra máxima directriz. Todos los destinos, presupuestos y coordenadas de tus viajes están encriptados. No vendemos información de nuestros 'Elite Members' a agencias externas."
            )

            TermSection(
                title = "4. Propiedad del 'Verde Mamba'",
                body = "Todo el código, diseño asimétrico y la paleta de colores (específicamente el hexágono #2DB300) son propiedad intelectual exclusiva de MambaJet Inc. Queda prohibida la ingeniería inversa de nuestro software."
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// Sub-componente para que el código quede muy limpio
@Composable
fun TermSection(title: String, body: String) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = body,
            fontSize = 14.sp,
            color = Color.DarkGray,
            lineHeight = 22.sp
        )
    }
}
