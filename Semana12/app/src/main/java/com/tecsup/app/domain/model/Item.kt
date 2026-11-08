package com.tecsup.app.domain.model

data class Item(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String
)