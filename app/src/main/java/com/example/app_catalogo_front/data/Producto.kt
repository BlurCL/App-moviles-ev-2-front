package com.example.app_catalogo_front.data

import java.math.BigDecimal

data class Producto(
    val codigo: Int,
    val categoria: String,
    val nombre: String,
    val precio: BigDecimal,
    val urlImagen: String?   // nullable porque en la BD tienes filas con null
)
