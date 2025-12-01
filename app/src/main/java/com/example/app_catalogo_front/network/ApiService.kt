package com.example.app_catalogo_front.network

import com.example.app_catalogo_front.data.Producto
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // 1. Listar productos (Ya lo tenías)
    @GET("productos")
    suspend fun getProductos(): List<Producto>

    // 2. Crear producto (Nuevo)
    @POST("productos")
    suspend fun crearProducto(@Body producto: Producto): Producto

    // 3. Editar producto (Nuevo)
    @PUT("productos/{codigo}")
    suspend fun actualizarProducto(
        @Path("codigo") codigo: Int,
        @Body producto: Producto
    ): Producto

    // 4. Borrar producto (Nuevo)
    @DELETE("productos/{codigo}")
    suspend fun eliminarProducto(@Path("codigo") codigo: Int): Response<Void>
}