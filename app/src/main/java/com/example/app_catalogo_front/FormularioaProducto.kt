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
    onNavigateBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var precioStr by remember { mutableStateOf("") }
    var urlImagen by remember { mutableStateOf("") }

    var errorMensaje by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Nuevo Producto") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Ingresa los datos del pastel:", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))

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

            // Campo opcional para la URL de la imagen
            OutlinedTextField(
                value = urlImagen,
                onValueChange = { urlImagen = it },
                label = { Text("URL de imagen (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (errorMensaje != null) {
                Text(
                    text = errorMensaje!!,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (nombre.isBlank() || categoria.isBlank() || precioStr.isBlank()) {
                        errorMensaje = "Por favor completa todos los campos obligatorios"
                    } else {
                        try {
                            val precio = BigDecimal(precioStr)

                            val nuevoProducto = Producto(
                                codigo = 0,                      // el backend genera el ID
                                categoria = categoria,
                                nombre = nombre,
                                precio = precio,
                                urlImagen = urlImagen.ifBlank { null }
                            )

                            viewModel.agregarProducto(nuevoProducto) {
                                onNavigateBack()
                            }

                        } catch (_: NumberFormatException) {
                            errorMensaje = "El precio debe ser un número válido"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Producto")
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
