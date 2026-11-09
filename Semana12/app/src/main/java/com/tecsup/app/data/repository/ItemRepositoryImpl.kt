package com.tecsup.app.data.repository

import com.tecsup.app.data.local.ItemDao
import com.tecsup.app.data.local.ItemEntity
import com.tecsup.app.data.remote.ApiService
import com.tecsup.app.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ItemRepositoryImpl(
    private val apiService: ApiService,
    private val itemDao: ItemDao
) : ItemRepository {

    // 1. Mapeo de Room (Entity) hacia el modelo de la UI (Item)
    override val items: Flow<List<Item>> = itemDao.obtenerItems().map { entityList ->
        entityList.map { entity ->
            Item(
                id = entity.id,
                title = entity.title,
                price = entity.price,
                description = entity.description,
                category = entity.category,
                image = entity.image
            )
        }
    }

    // 2. Mapeo correcto usando las variables de tu ItemDto (titulo, descripcion, imagenUrl)
    override suspend fun refreshItems() {
        try {
            val remoteProducts = apiService.getItems()
            val entityList = remoteProducts.map { dto ->
                ItemEntity(
                    id = dto.id,
                    title = dto.titulo,
                    price = dto.price,
                    description = dto.descripcion,
                    category = dto.category,
                    image = dto.imagenUrl
                )
            }
            itemDao.insertarItems(entityList)
        } catch (e: Exception) {
            // AQUÍ IMPRIMIMOS EL ERROR REAL EN EL LOGCAT
            e.printStackTrace()
            println("ERROR_API_DEBUG: ${e.message}")
        }
    }
}