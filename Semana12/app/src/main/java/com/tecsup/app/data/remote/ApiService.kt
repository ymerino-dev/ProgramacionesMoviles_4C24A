package com.tecsup.app.data.remote

import com.tecsup.app.data.remote.dto.ItemDto
import com.tecsup.app.data.remote.dto.ItemResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    suspend fun obtenerProductos(): ItemResponseDto

    @GET("products/{id}")
    suspend fun obtenerDetalleProducto(@Path("id") id: Int): ItemDto
}