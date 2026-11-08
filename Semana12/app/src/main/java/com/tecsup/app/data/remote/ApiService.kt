package com.tecsup.app.data.remote

import com.tecsup.app.data.remote.dto.ItemDto
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getItems(): List<ItemDto>
}