package com.tecsup.app.data.mapper

import com.tecsup.app.data.local.ItemEntity
import com.tecsup.app.data.remote.dto.ItemDto
import com.tecsup.app.domain.model.Item

fun ItemDto.toEntity(esFavorito: Boolean = false): ItemEntity = ItemEntity(
    id = id,
    title = titulo,        // Apunta a la propiedad 'titulo' de tu ItemDto
    description = descripcion, // Apunta a la propiedad 'descripcion' de tu ItemDto
    price = price,
    category = category,
    image = imagenUrl,     // Apunta a la propiedad 'imagenUrl' (o thumbnail) de tu ItemDto
    esFavorito = esFavorito
)

fun ItemEntity.toDomain(): Item = Item(
    id = id,
    title = title,
    description = description,
    price = price,
    category = category,
    image = image
)