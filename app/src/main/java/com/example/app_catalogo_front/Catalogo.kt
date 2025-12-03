package com.example.app_catalogo_front

import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app_catalogo_front.data.CarritoManager
import com.example.app_catalogo_front.data.Producto
import com.example.app_catalogo_front.viewmodel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    modifier: Modifier = Modifier,
    viewModel: CatalogoViewModel,
    esAdmin: Boolean = false
) {
    val productos by viewModel.productos.collectAsState()
    val valorDolar by viewModel.valorDolar.collectAsState()
    val context = LocalContext.current // Necesitamos el contexto para vibrar y navegar

    // ESTADO PARA SABER SI ESTAMOS EDITANDO O CREANDO
    var mostrarFormulario by remember { mutableStateOf(false) }
    var productoAEditar by remember { mutableStateOf<Producto?>(null) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = colorResource(id = R.color.app_background)
    ) {
        // Si hay que mostrar formulario (ya sea para crear o editar)
        if (mostrarFormulario && esAdmin) {
            FormularioProductoScreen(
                viewModel = viewModel,
                productoAEditar = productoAEditar, // Pasamos el producto (puede ser null)
                onNavigateBack = {
                    mostrarFormulario = false
                    productoAEditar = null // Limpiamos al volver
                }
            )
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Column {
                                Text("Pastelería Mil Sabores")
                                if (valorDolar != null) {
                                    Text(
                                        text = "Dólar: $${valorDolar}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                context.startActivity(Intent(context, CarritoActivity::class.java))
                            }) {
                                Icon(Icons.Default.ShoppingCart, "Carrito", tint = Color(0xFF4E342E))
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                },
                floatingActionButton = {
                    if (esAdmin) {
                        FloatingActionButton(
                            onClick = {
                                // VIBRACIÓN AL TOCAR EL BOTÓN FLOTANTE
                                vibrar(context)
                                productoAEditar = null // Aseguramos que es NUEVO
                                mostrarFormulario = true
                            },
                            containerColor = Color(0xFF795548),
                            contentColor = Color.White
                        ) {
                            Icon(Icons.Default.Add, "Agregar")
                        }
                    }
                },
                containerColor = Color.Transparent
            ) { paddingValues ->
                if (productos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay productos aún", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(productos, key = { it.codigo }) { producto ->
                            ProductoCard(
                                producto = producto,
                                esAdmin = esAdmin,
                                onEliminar = {
                                    // VIBRACIÓN AL ELIMINAR
                                    vibrar(context)
                                    viewModel.eliminarProducto(producto.codigo)
                                },
                                onEditar = {
                                    // VIBRACIÓN AL EDITAR
                                    vibrar(context)
                                    productoAEditar = producto // Guardamos cual editar
                                    mostrarFormulario = true   // Abrimos formulario
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoCard(
    producto: Producto,
    esAdmin: Boolean,
    onEliminar: () -> Unit,
    onEditar: () -> Unit
) {
    // Obtenemos el contexto aquí también por si acaso
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Imagen (si existe)
            if (!producto.urlImagen.isNullOrBlank()) {
                AsyncImage(
                    model = producto.urlImagen,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Datos
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF5D4037)
                )
                Text(
                    text = producto.categoria,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "$${producto.precio}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color(0xFF795548)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        // VIBRACIÓN AL AGREGAR AL CARRITO
                        vibrar(context)
                        CarritoManager.agregarProducto(producto)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC0CB),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar")
                }
            }

            // Botones de Admin (Editar y Eliminar)
            if (esAdmin) {
                Column {
                    // BOTÓN EDITAR (Lápiz)
                    IconButton(onClick = onEditar) {
                        Icon(Icons.Default.Edit, "Editar", tint = Color(0xFF1976D2)) // Azul
                    }
                    // BOTÓN ELIMINAR (Basura)
                    IconButton(onClick = onEliminar) {
                        Icon(Icons.Default.Delete, "Eliminar", tint = Color(0xFFD32F2F)) // Rojo
                    }
                }
            }
        }
    }
}