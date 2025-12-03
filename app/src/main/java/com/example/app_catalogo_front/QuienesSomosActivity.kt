package com.example.app_catalogo_front

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_catalogo_front.ui.theme.AppcatalogofrontTheme

class QuienesSomosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppcatalogofrontTheme {
                QuienesSomosScreen(onBackPressed = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuienesSomosScreen(onBackPressed: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuestra Historia", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF0F5), // Un color rosadito pastel para la barra
                    titleContentColor = Color(0xFF5D4037) // Color café para el texto
                )
            )
        }
    ) { innerPadding ->
        // Agregamos 'verticalScroll' para que no se corte el texto en celulares pequeños
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF8E1)) // Fondo crema suave
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()) // Scroll activado
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- LOGO / ÍCONO ---
            // --- LOGO / ÍCONO ---
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFCCBC)),
                contentAlignment = Alignment.Center
            ) {
                // CAMBIO: Usamos Star en lugar de Cake para evitar errores
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Logo Pastelería",
                    modifier = Modifier.size(60.dp),
                    tint = Color(0xFFD84315)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- TÍTULO ---
            Text(
                text = "Pastelería Mil Sabores",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF5D4037),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Horneando felicidad desde 2010",
                style = MaterialTheme.typography.labelLarge,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF8D6E63),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- HISTORIA ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "En \"Mil Sabores\", creemos que la vida es mejor con un toque dulce. Lo que comenzó como un pequeño sueño en la cocina de la abuela, hoy es un rincón donde la tradición y la creatividad se unen.\n\nCada uno de nuestros pasteles, tartas y galletas está elaborado a mano con ingredientes 100% naturales, sin conservantes y con el ingrediente secreto más importante: mucho amor. ¡Gracias por dejarnos ser parte de tus celebraciones!",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF4E342E)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- EQUIPO ---
            Text(
                text = "El Equipo Creativo",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D4037)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de Integrantes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Integrante 1
                IntegranteCard(nombre = "Consuelo Jerez", rol = "Maestra Pastelera")

                // Integrante 2
                IntegranteCard(nombre = "Cristóbal Venegas", rol = "Jefe de Cocina")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Componente pequeño para mostrar a cada persona (Tarjeta reutilizable)
@Composable
fun IntegranteCard(nombre: String, rol: String) {
    Card(
        modifier = Modifier.width(150.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFAB91)), // Color salmón
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Star, // Una estrellita para cada uno
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Text(
                text = rol,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFFFBE9E7)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuienesSomosScreenPreview() {
    AppcatalogofrontTheme {
        QuienesSomosScreen(onBackPressed = {})
    }
}