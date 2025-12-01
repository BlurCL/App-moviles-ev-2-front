package com.example.app_catalogo_front

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_catalogo_front.data.Producto
import com.example.app_catalogo_front.viewmodel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(modifier: Modifier = Modifier, viewModel: CatalogoViewModel) {
    val productos by viewModel.productos.collectAsState()
    val valorDolar by viewModel.valorDolar.collectAsState() // Leemos el dólar

    var mostrarFormulario by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = colorResource(id = R.color.app_background)
    ) {
        if (mostrarFormulario) {
            FormularioProductoScreen(
                viewModel = viewModel,
                onNavigateBack = { mostrarFormulario = false }
            )
        } else {
            Scaffold(
                // BARRA SUPERIOR CON EL DÓLAR
                topBar = {
                    TopAppBar(
                        title = { Text("Pastelería Mil Sabores") },
                        actions = {
                            if (valorDolar != null) {
                                Text(
                                    text = "Dólar hoy: $${valorDolar}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF4E342E),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 16.dp)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White
                        )
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { mostrarFormulario = true },
                        containerColor = Color(0xFF795548),
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
                    }
                },
                containerColor = Color.Transparent
            ) { paddingValues ->
                Crossfade(
                    targetState = productos.isEmpty(),
                    label = "ContentFade",
                    animationSpec = tween(durationMillis = 1000),
                    modifier = Modifier.padding(paddingValues)
                ) { vacio ->
                    if (vacio) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = Color(0xFF4E342E))
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Cargando...", color = Color(0xFF6D4C41))
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(productos, key = { prod -> prod.codigo }) { producto ->
                                ProductoCard(
                                    producto = producto,
                                    onEliminar = { viewModel.eliminarProducto(producto.codigo) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: Producto, onEliminar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF5D4037)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Categoría: ${producto.categoria}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${producto.precio}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color(0xFF795548)
                )
            }
            IconButton(onClick = { onEliminar() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFD32F2F)
                )
            }
        }
    }
}