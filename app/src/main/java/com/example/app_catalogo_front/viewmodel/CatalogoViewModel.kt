package com.example.app_catalogo_front.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_catalogo_front.data.Producto
import com.example.app_catalogo_front.network.RetrofitInstance
import com.example.app_catalogo_front.network.RetrofitIndicadores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {

    // Lista de productos
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    // NUEVO: Aquí guardaremos el valor del Dolar
    private val _valorDolar = MutableStateFlow<Double?>(null)
    val valorDolar: StateFlow<Double?> = _valorDolar

    init {
        fetchProductos()
        fetchDolar() // Al iniciar, buscamos el precio del dólar
    }

    // --- API EXTERNA (Dolar) ---
    fun fetchDolar() {
        viewModelScope.launch {
            try {
                // Llamamos a la API de mindicador.cl
                val respuesta = RetrofitIndicadores.api.obtenerIndicadores()
                _valorDolar.value = respuesta.dolar.valor
            } catch (e: Exception) {
                e.printStackTrace() // Si falla, simplemente no se mostrará
            }
        }
    }

    // --- TUS PRODUCTOS (CRUD Local) ---
    fun fetchProductos() {
        viewModelScope.launch {
            try {
                _productos.value = RetrofitInstance.api.getProductos()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun agregarProducto(producto: Producto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.crearProducto(producto)
                fetchProductos()
                onSuccess()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun actualizarProducto(codigo: Int, producto: Producto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.actualizarProducto(codigo, producto)
                fetchProductos()
                onSuccess()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun eliminarProducto(codigo: Int) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.eliminarProducto(codigo)
                fetchProductos()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}