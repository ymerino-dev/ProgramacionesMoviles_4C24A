package com.tecsup.app.data.repository

import com.tecsup.app.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    val items: Flow<List<Item>>
    suspend fun refreshItems()
}