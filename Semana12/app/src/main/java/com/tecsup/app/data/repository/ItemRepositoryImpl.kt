package com.tecsup.app.data.repository

import com.tecsup.app.data.local.ItemDao
import com.tecsup.app.data.local.ItemEntity
import com.tecsup.app.data.remote.ApiService
import com.tecsup.app.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import retrofit2.HttpException

class ItemRepositoryImpl(
    private val apiService: ApiService,
    private val itemDao: ItemDao
) : ItemRepository {

    override val items: Flow<List<Item>> = itemDao.todos().map { entityList ->
        entityList.map { entity ->
            Item(
                id = entity.id,
                title = entity.titulo,
                description = entity.subtitulo,
                price = 0.0,
                category = "General",
                image = entity.imagenUrl
            )
        }
    }

    override val favoritos: Flow<List<Item>> = itemDao.favoritos().map { entityList ->
        entityList.map { entity ->
            Item(
                id = entity.id,
                title = entity.titulo,
                description = entity.subtitulo,
                price = 0.0,
                category = "General",
                image = entity.imagenUrl
            )
        }
    }

    override suspend fun refrescar(): Result<Int> = try {
        val favoritosIds = itemDao.idsFavoritos().toSet()
        val response = apiService.obtenerProductos() // Ahora devuelve ItemResponseDto
        val nuevos = response.productos.map { dto ->
            ItemEntity(
                id = dto.id,
                titulo = dto.titulo,
                subtitulo = dto.descripcion,
                imagenUrl = dto.imagenUrl,
                esFavorito = dto.id in favoritosIds
            )
        }
        itemDao.guardarTodos(items = nuevos)
        Result.success(value = nuevos.size)
    } catch (e: IOException) {
        Result.failure(exception = e)
    } catch (e: HttpException) {
        Result.failure(exception = e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        val dto = apiService.obtenerDetalleProducto(id)
        val item = Item(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            price = dto.price,
            category = dto.category,
            image = dto.thumbnail
        )
        Result.success(item)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) {
        itemDao.alternarFavorito(id)
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