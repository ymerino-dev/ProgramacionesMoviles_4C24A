package com.tecsup.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val id: Int,

    @SerialName("title")
    val titulo: String,

    @SerialName("description")
    val descripcion: String,

    @SerialName("thumbnail")
    val imagenUrl: String = "", // <--- Añadimos valor por defecto para que no falle si falta

    val price: Double,

    val category: String
)

@Serializable
data class ItemResponseDto(
    @SerialName("products")
    val productos: List<ItemDto>
)