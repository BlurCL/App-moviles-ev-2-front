package com.example.app_catalogo_front

import com.example.app_catalogo_front.data.CarritoManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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

    var mostrarFormulario by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = colorResource(id = R.color.app_background)
    ) {
        if (mostrarFormulario && esAdmin) {
            FormularioProductoScreen(
                viewModel = viewModel,
                onNavigateBack = { mostrarFormulario = false }
            )
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Pastelería Mil Sabores")
                                if (valorDolar != null) {
                                    Text(
                                        text = "Dólar hoy: $${valorDolar}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4E342E)
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                },
                floatingActionButton = {
                    if (esAdmin) {
                        FloatingActionButton(
                            onClick = { mostrarFormulario = true },
                            containerColor = Color(0xFF795548),
                            contentColor = Color.White
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Agregar"
                            )
                        }
                    }
                },
                containerColor = Color.Transparent
            ) { paddingValues ->

                if (productos.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay productos aún.\n¡Agrega el primero!",
                            textAlign = TextAlign.Center,
                            color = Color.Gray,
                            fontSize = 18.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(productos, key = { prod -> prod.codigo }) { producto ->
                            ProductoCard(
                                producto = producto,
                                esAdmin = esAdmin,
                                onEliminar = { viewModel.eliminarProducto(producto.codigo) }
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
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 👇 IMAGEN DEL PRODUCTO (SI TIENE URL)
            if (!producto.urlImagen.isNullOrBlank()) {
                AsyncImage(
                    model = producto.urlImagen,
                    contentDescription = "Imagen de ${producto.nombre}",
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // TEXTO + BOTÓN
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF5D4037)
                )
                Text(
                    text = "Categoría: ${producto.categoria}",
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

                // 👇 BOTÓN AGREGAR AL CARRITO (CLIENTE / ADMIN, DA IGUAL)
                Button(
                    onClick = { CarritoManager.agregarProducto(producto) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC0CB),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Agregar al carrito")
                }
            }


            // BOTÓN ELIMINAR SOLO PARA ADMIN
            if (esAdmin) {
                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFD32F2F)
                    )
                }
            }
        }
    }
}
