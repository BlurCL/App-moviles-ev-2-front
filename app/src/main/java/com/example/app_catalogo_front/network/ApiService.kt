package com.example.app_catalogo_front.network

import com.example.app_catalogo_front.data.Producto
import retrofit2.http.GET

interface ApiService {
    @GET("productos")
    suspend fun getProductos(): List<Producto>
}
