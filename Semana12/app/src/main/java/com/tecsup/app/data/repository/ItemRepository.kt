package com.tecsup.app.data.repository

import com.tecsup.app.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    val items: Flow<List<Item>>
    val favoritos: Flow<List<Item>>
    suspend fun refrescar(): Result<Int>
    suspend fun obtenerDetalle(id: Int): Result<Item>
    suspend fun alternarFavorito(id: Int)
    suspend fun quitarTodosFavoritos()
    suspend fun agregarProducto(title: String, price: Double, category: String, description: String, image: String): Result<Boolean>
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
}
