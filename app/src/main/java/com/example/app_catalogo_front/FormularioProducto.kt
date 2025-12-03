package com.example.app_catalogo_front

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.app_catalogo_front.data.Producto
import com.example.app_catalogo_front.viewmodel.CatalogoViewModel
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoScreen(
    viewModel: CatalogoViewModel,
    productoAEditar: Producto? = null,
    onNavigateBack: () -> Unit
) {
    // 1. Agregamos la variable para la URL
    var nombre by remember { mutableStateOf(productoAEditar?.nombre ?: "") }
    var categoria by remember { mutableStateOf(productoAEditar?.categoria ?: "") }
    var precioStr by remember { mutableStateOf(productoAEditar?.precio?.toString() ?: "") }
    var urlImagen by remember { mutableStateOf(productoAEditar?.urlImagen ?: "") } // <--- NUEVO

    var errorMensaje by remember { mutableStateOf<String?>(null) }

    val titulo = if (productoAEditar != null) "Editar Producto" else "Nuevo Producto"

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(titulo) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = precioStr,
                onValueChange = { precioStr = it },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 2. Campo nuevo para la URL de la imagen
            OutlinedTextField(
                value = urlImagen,
                onValueChange = { urlImagen = it },
                label = { Text("URL de Imagen (Opcional)") },
                placeholder = { Text("http://...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (errorMensaje != null) {
                Text(text = errorMensaje!!, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (nombre.isBlank() || categoria.isBlank() || precioStr.isBlank()) {
                        errorMensaje = "Completa los campos obligatorios"
                    } else {
                        try {
                            val precio = BigDecimal(precioStr)

                            // 3. Enviamos la URL al crear o editar
                            if (productoAEditar == null) {
                                // MODO CREAR
                                val nuevo = Producto(
                                    codigo = 0,
                                    categoria = categoria,
                                    nombre = nombre,
                                    precio = precio,
                                    urlImagen = urlImagen.ifBlank { null } // Si está vacío, mandamos null
                                )
                                viewModel.agregarProducto(nuevo) { onNavigateBack() }
                            } else {
                                // MODO EDITAR
                                val editado = productoAEditar.copy(
                                    nombre = nombre,
                                    categoria = categoria,
                                    precio = precio,
                                    urlImagen = urlImagen.ifBlank { null }
                                )
                                viewModel.actualizarProducto(editado.codigo, editado) { onNavigateBack() }
                            }
                        } catch (e: Exception) {
                            errorMensaje = "Precio inválido"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (productoAEditar != null) "Guardar Cambios" else "Crear Producto")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { onNavigateBack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}