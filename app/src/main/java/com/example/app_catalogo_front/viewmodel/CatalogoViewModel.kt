package com.example.app_catalogo_front.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_catalogo_front.data.Producto
import com.example.app_catalogo_front.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    init {
        viewModelScope.launch {
            try {
                _productos.value = RetrofitInstance.api.getProductos()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
