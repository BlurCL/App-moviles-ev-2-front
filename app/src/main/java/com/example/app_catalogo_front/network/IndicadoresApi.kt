package com.example.app_catalogo_front.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class RespuestaIndicadores(
    val dolar: Indicador
)

data class Indicador(
    val valor: Double
)

interface IndicadoresApi {
    @GET("api")
    suspend fun obtenerIndicadores(): RespuestaIndicadores
}

object RetrofitIndicadores {
    val api: IndicadoresApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://mindicador.cl/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IndicadoresApi::class.java)
    }
}