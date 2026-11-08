package com.tecsup.app.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory // O el import correcto de kotlinx serialization
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://fakestoreapi.com/"

    val apiService: ApiService by lazy {
        val contentType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(contentType))
            .build()
            .create(ApiService::class.java)
    }
}