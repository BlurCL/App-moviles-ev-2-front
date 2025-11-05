package com.example.app_catalogo_front

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_catalogo_front.data.Producto
import com.example.app_catalogo_front.viewmodel.CatalogoViewModel

@Composable
fun CatalogoScreen(modifier: Modifier = Modifier, viewModel: CatalogoViewModel) {
    val productos by viewModel.productos.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFFFF5E1) // fondo
    ) {
        Crossfade(targetState = productos.isEmpty(), label = "ContentFade", animationSpec = tween(durationMillis = 1000)) {
            if (it) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFF4E342E))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Cargando productos...", color = Color(0xFF6D4C41), fontSize = 16.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(productos, key = { prod -> prod.codigo }) { producto ->
                        ProductoCard(producto)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: Producto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
    }
}
