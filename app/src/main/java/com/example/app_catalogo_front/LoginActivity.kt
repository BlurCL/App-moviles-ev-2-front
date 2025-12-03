package com.example.app_catalogo_front

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.app_catalogo_front.ui.theme.AppcatalogofrontTheme

class LoginActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppcatalogofrontTheme {
                Scaffold(
                    containerColor = Color(0xFF4E342E), // Fondo marrón oscuro
                    topBar = {
                        TopAppBar(
                            title = { Text("Iniciar Sesión", color = Color.White) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                                navigationIconContentColor = Color.White
                            ),
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Volver"
                                    )
                                }
                            }
                        )
                    }
                ) {
                    LoginScreen(Modifier.padding(it))
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {

    // Ya tienes la referencia al contexto aquí, ¡perfecto!
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "ScaleAnim"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Bienvenido de Nuevo 👋",
                    style = MaterialTheme.typography.headlineMedium
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMensaje != null) {
                    Text(
                        text = errorMensaje!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = {
                        // LOGIN SIMPLIFICADO
                        val (esValido, esAdmin) = when {
                            username == "admin" && password == "admin123" -> true to true
                            username == "cliente" && password == "cliente123" -> true to false
                            else -> false to false
                        }

                        if (!esValido) {
                            errorMensaje = "Usuario o contraseña incorrectos"
                        } else {
                            errorMensaje = null
                            // Se usa 'context' para crear el Intent y lanzar la Activity
                            val intent = Intent(context, CatalogoActivity::class.java).apply {
                                putExtra("esAdmin", true)   // Se mantiene tu lógica para forzar el modo admin
                            }
                            context.startActivity(intent)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC0CB) // Rosadito
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                    interactionSource = interactionSource
                ) {
                    Text("Ingresar", color = Color.Black)
                }

                // Ayuda para el profe / pruebas
                Text(
                    text = "admin / admin123 (admin)\ncliente / cliente123 (cliente)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}
