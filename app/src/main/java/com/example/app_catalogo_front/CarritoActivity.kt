package com.example.app_catalogo_front

import android.os.Bundle
import android.widget.Toast // <--- IMPORTANTE: Para el mensaje de "Pago Exitoso"
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // <--- IMPORTANTE: Para saber dónde estamos
import androidx.compose.ui.unit.dp
import com.example.app_catalogo_front.data.CarritoManager
import com.example.app_catalogo_front.ui.theme.AppcatalogofrontTheme

class CarritoActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppcatalogofrontTheme {
                // Obtenemos el contexto para poder lanzar el mensaje
                val context = LocalContext.current

                val items by CarritoManager.items.collectAsState()
                val total = remember(items) { CarritoManager.total() }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Mi carrito") },
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
                ) { padding ->

                    if (items.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tu carrito está vacío")
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            // LISTA DE PRODUCTOS (Ocupa el espacio disponible con weight)
                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(items) { item ->
                                    CarritoItemCard(
                                        item = item,
                                        onIncrement = { CarritoManager.agregarProducto(item.producto) },
                                        onDecrement = { CarritoManager.disminuirCantidad(item.producto) },
                                        onRemove = { CarritoManager.eliminarProducto(item.producto) }
                                    )
                                }
                            }

                            HorizontalDivider()

                            // TOTAL A PAGAR
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total:", style = MaterialTheme.typography.titleMedium)
                                Text(
                                    text = "$${total.toInt()}", // Sin decimales
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }

                            // BOTÓN DE PAGAR
                            Button(
                                onClick = {
                                    // 1. Mostrar mensaje de éxito (Toast)
                                    Toast.makeText(context, "¡Pago realizado con éxito!", Toast.LENGTH_LONG).show()

                                    // 2. Vaciar el carrito
                                    CarritoManager.vaciar()

                                    // 3. Volver al catálogo
                                    finish()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text("Pagar $$total")
                            }
                        }
                    }
                }
            }
        }
    }
}