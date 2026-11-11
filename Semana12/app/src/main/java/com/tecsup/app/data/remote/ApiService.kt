package com.tecsup.app.data.remote

import com.tecsup.app.data.remote.dto.ItemDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    suspend fun obtenerProductos(): List<ItemDto>

    @GET("products/{id}")
    suspend fun obtenerDetalleProducto(@Path("id") id: Int): ItemDto
}
