package com.example.app_catalogo_front

import android.os.Bundle
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
import androidx.compose.ui.unit.dp
import com.example.app_catalogo_front.data.CarritoManager
import com.example.app_catalogo_front.ui.theme.AppcatalogofrontTheme

class CarritoActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppcatalogofrontTheme {

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
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(items) { item ->
                                    CarritoItemCard(
                                        item = item,
                                        onIncrement = {
                                            // sumar 1 unidad
                                            CarritoManager.agregarProducto(item.producto)
                                        },
                                        onDecrement = {
                                            // restar 1 unidad
                                            CarritoManager.disminuirCantidad(item.producto)
                                        },
                                        onRemove = {
                                            // eliminar del carrito
                                            CarritoManager.eliminarProducto(item.producto)
                                        }
                                    )
                                }
                            }


                            HorizontalDivider()

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total:", style = MaterialTheme.typography.titleMedium)
                                Text(
                                    text = "$$total",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }

                            Button(
                                onClick = { CarritoManager.vaciar() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text("Finalizar compra")
                            }
                        }
                    }
                }
            }
        }
    }
}
