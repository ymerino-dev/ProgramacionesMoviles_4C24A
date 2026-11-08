package com.tecsup.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String
)