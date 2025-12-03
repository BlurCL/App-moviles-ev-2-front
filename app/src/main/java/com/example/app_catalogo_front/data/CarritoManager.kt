package com.example.app_catalogo_front.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Ítem del carrito: producto + cantidad
data class CartItem(
    val producto: Producto,
    val cantidad: Int
)

object CarritoManager {

    // Estado del carrito
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items

    // Agregar 1 unidad del producto
    fun agregarProducto(producto: Producto) {
        val listaActual = _items.value.toMutableList()
        val index = listaActual.indexOfFirst { it.producto.codigo == producto.codigo }

        if (index >= 0) {
            val actual = listaActual[index]
            listaActual[index] = actual.copy(cantidad = actual.cantidad + 1)
        } else {
            listaActual.add(CartItem(producto, 1))
        }

        _items.value = listaActual

        android.util.Log.d("CARRITO", "Items en carrito: ${_items.value.size}")
    }


    // Quitar 1 unidad del producto (o eliminarlo si queda en 0)
    fun eliminarProducto(producto: Producto) {
        val listaActual = _items.value.toMutableList()

        val index = listaActual.indexOfFirst { it.producto.codigo == producto.codigo }
        if (index >= 0) {
            val item = listaActual[index]
            if (item.cantidad > 1) {
                listaActual[index] = item.copy(cantidad = item.cantidad - 1)
            } else {
                listaActual.removeAt(index)
            }
        }

        _items.value = listaActual
    }

    // Vaciar carrito
    fun vaciar() {
        _items.value = emptyList()
    }

    // Total del carrito (asumo precio: Double en Producto)
    fun total(): Double {
        return _items.value.sumOf { item ->
            item.producto.precio.toDouble() * item.cantidad
        }
    }

    fun disminuirCantidad(producto: Producto) {
        val listaActual = _items.value.toMutableList()
        val index = listaActual.indexOfFirst { it.producto.codigo == producto.codigo }

        if (index >= 0) {
            val item = listaActual[index]
            val nuevaCantidad = item.cantidad - 1

            if (nuevaCantidad <= 0) {
                listaActual.removeAt(index)
            } else {
                listaActual[index] = item.copy(cantidad = nuevaCantidad)
            }

            _items.value = listaActual
        }
    }



}
