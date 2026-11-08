package com.tecsup.app.data.repository

import com.tecsup.app.data.local.ItemDao
import com.tecsup.app.data.mapper.*
import com.tecsup.app.data.remote.ApiService
import com.tecsup.app.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ItemRepositoryImpl(
    private val apiService: ApiService,
    private val itemDao: ItemDao
) : ItemRepository {

    override val items: Flow<List<Item>> = itemDao.obtenerItems().map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun refreshItems() {
        try {
            val remoteDtos = apiService.getItems()
            val entities = remoteDtos.map { it.toEntity() }
            itemDao.insertarItems(entities)
        } catch (e: Exception) {
            // Si no hay red, la app se mantiene con los datos locales de Room
        }
    }
}