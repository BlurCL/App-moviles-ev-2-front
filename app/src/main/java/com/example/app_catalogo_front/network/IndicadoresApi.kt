package com.example.app_catalogo_front.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// 1. Lo que recibimos de internet (Solo nos interesa el Dolar)
data class RespuestaIndicadores(
    val dolar: Indicador
)

data class Indicador(
    val valor: Double
)

// 2. La interfaz de conexión
interface IndicadoresApi {
    @GET("api")
    suspend fun obtenerIndicadores(): RespuestaIndicadores
}

// 3. El objeto para usarlo
object RetrofitIndicadores {
    val api: IndicadoresApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://mindicador.cl/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IndicadoresApi::class.java)
    }
}