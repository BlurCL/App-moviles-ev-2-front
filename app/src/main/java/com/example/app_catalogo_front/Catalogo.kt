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
import androidx.compose.ui.text.style.TextAlign
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

    var mostrarFormulario by remember { mutableStateOf(false) }

    // Obtenemos el contexto para poder navegar al Carrito
    val context = LocalContext.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = colorResource(id = R.color.app_background)
    ) {
        // Si estamos en modo Admin y se activó el formulario, lo mostramos
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
                            Column {
                                Text("Pastelería Mil Sabores")
                                if (valorDolar != null) {
                                    Text(
                                        text = "Dólar hoy: $${valorDolar}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4E342E),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        },
                        // BOTÓN DEL CARRITO EN LA BARRA SUPERIOR
                        actions = {
                            IconButton(onClick = {
                                val intent = Intent(context, CarritoActivity::class.java)
                                context.startActivity(intent)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Ver Carrito",
                                    tint = Color(0xFF4E342E)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                },
                // BOTÓN FLOTANTE (+) SOLO PARA ADMIN
                floatingActionButton = {
                    if (esAdmin) {
                        FloatingActionButton(
                            onClick = { mostrarFormulario = true },
                            containerColor = Color(0xFF795548),
                            contentColor = Color.White
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Agregar Producto"
                            )
                        }
                    }
                },
                containerColor = Color.Transparent
            ) { paddingValues ->

                Crossfade(
                    targetState = productos.isEmpty(),
                    label = "ContentFade",
                    animationSpec = tween(durationMillis = 500),
                    modifier = Modifier.padding(paddingValues)
                ) { vacio ->
                    if (vacio) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
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
                            modifier = Modifier.fillMaxSize(),
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

            // IMAGEN DEL PRODUCTO (SI TIENE URL)
            // (Si no usas URL en tu formulario, esto no se mostrará, no te preocupes)
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


            // TEXTO + BOTÓN AGREGAR
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

                // BOTÓN "AGREGAR AL CARRITO" (Visible para todos)
                Button(
                    onClick = { CarritoManager.agregarProducto(producto) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC0CB), // Rosado pastel
                        contentColor = Color.Black
                    )
                ) {
                    Text("Agregar al carrito")
                }
            }

            // BOTÓN ELIMINAR (SOLO PARA ADMIN)
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