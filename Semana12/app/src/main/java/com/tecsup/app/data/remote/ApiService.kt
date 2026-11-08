package com.tecsup.app.data.remote

import com.tecsup.app.data.remote.dto.ItemDto
import retrofit2.http.GET

interface ApiService {
    @GET("products") // O la ruta de tu API del laboratorio
    suspend fun getItems(): List<ItemDto>
}