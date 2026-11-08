package com.tecsup.app.data.mapper

import com.tecsup.app.data.local.ItemEntity
import com.tecsup.app.data.remote.dto.ItemDto
import com.tecsup.app.domain.model.Item

fun ItemDto.toEntity(): ItemEntity = ItemEntity(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image
)

fun ItemEntity.toDomain(): Item = Item(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image
)

fun ItemDto.toDomain(): Item = Item(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image
)