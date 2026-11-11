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

    override val items: Flow<List<Item>> = itemDao.todos().map { entityList ->
        entityList.map { entity ->
            Item(
                id = entity.id,
                title = entity.title,
                description = entity.description,
                price = entity.price,
                category = entity.category,
                image = entity.image
            )
        }
    }

    override val favoritos: Flow<List<Item>> = itemDao.favoritos().map { entityList ->
        entityList.map { entity ->
            Item(
                id = entity.id,
                title = entity.title,
                description = entity.description,
                price = entity.price,
                category = entity.category,
                image = entity.image
            )
        }
    }

    override suspend fun refrescar(): Result<Int> = try {
        val favoritosIds = itemDao.idsFavoritos().toSet()
        val listaDto = apiService.obtenerProductos()
        val nuevos = listaDto.map { dto ->
            ItemEntity(
                id = dto.id,
                title = dto.titulo,
                description = dto.descripcion,
                price = dto.price,
                category = dto.category,
                image = dto.imagenUrl,
                esFavorito = dto.id in favoritosIds
            )
        }
        itemDao.guardarTodos(items = nuevos)
        Result.success(value = nuevos.size)
    } catch (e: Exception) {
        Result.failure(exception = e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        val dto = apiService.obtenerDetalleProducto(id)
        val item = Item(
            id = dto.id,
            title = dto.titulo,
            description = dto.descripcion,
            price = dto.price,
            category = dto.category,
            image = dto.imagenUrl
        )
        Result.success(value = item)
    } catch (e: Exception) {
        Result.failure(exception = e)
    }

    override suspend fun alternarFavorito(id: Int) {
        itemDao.alternarFavorito(id)
    }

    override suspend fun quitarTodosFavoritos() {
        itemDao.quitarTodosFavoritos()
    }

    override suspend fun cantidadGuardada(): Int {
        return itemDao.contar()
    }

    override suspend fun cantidadFavoritos(): Int {
        return itemDao.contarFavoritos()
    }

    override suspend fun limpiarCache() {
        itemDao.borrarTodo()
    }
}
