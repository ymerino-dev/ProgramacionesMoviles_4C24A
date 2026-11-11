package com.tecsup.app.ui.lista

import com.tecsup.app.data.repository.ItemRepository
import com.tecsup.app.domain.model.Item
import com.tecsup.app.notification.Notificador
import com.tecsup.app.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeItemRepository
    private lateinit var fakeNotificador: FakeNotificador
    private lateinit var viewModel: ListaViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeItemRepository()
        fakeNotificador = FakeNotificador()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialLoadEmitsItemsFromRepository() = runTest {
        // Given
        val sampleItems = listOf(
            Item(1, "Smartphone", 299.99, "A phone", "smartphones", "url1"),
            Item(2, "Laptop", 899.99, "A laptop", "laptops", "url2")
        )
        fakeRepository.setInitialItems(sampleItems)

        // When
        viewModel = ListaViewModel(fakeRepository, fakeNotificador)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val estado = viewModel.estado.value
        assertTrue(estado is UiState.Exito)
        assertEquals(sampleItems, (estado as UiState.Exito).datos)
    }

    @Test
    fun testCargarProductosInvokesRefrescar() = runTest {
        // Given
        fakeRepository.resetCallCount()
        viewModel = ListaViewModel(fakeRepository, fakeNotificador)
        testDispatcher.scheduler.advanceUntilIdle()

        val initialCount = fakeRepository.refreshCallCount

        // When
        viewModel.cargarProductos()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(initialCount + 1, fakeRepository.refreshCallCount)
    }

    @Test
    fun testRepositoryEmptyItemsEmitsErrorWhenOffline() = runTest {
        // Given
        fakeRepository.setInitialItems(emptyList())
        fakeRepository.shouldFail = true

        // When
        viewModel = ListaViewModel(fakeRepository, fakeNotificador)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val estado = viewModel.estado.value
        assertTrue(estado is UiState.Error)
    }

    private class FakeNotificador : Notificador {
        override fun mostrarNotificacion(titulo: String, mensaje: String) {
            // No-op for test
        }
    }

    private class FakeItemRepository : ItemRepository {
        private val _items = MutableStateFlow<List<Item>>(emptyList())
        override val items: Flow<List<Item>> = _items
        private val _favoritos = MutableStateFlow<List<Item>>(emptyList())
        override val favoritos: Flow<List<Item>> = _favoritos

        var refreshCallCount = 0
        var shouldFail = false
        private var itemsToReturn: List<Item> = emptyList()

        fun setInitialItems(list: List<Item>) {
            itemsToReturn = list
            _items.value = list
        }

        fun resetCallCount() {
            refreshCallCount = 0
            shouldFail = false
        }

        override suspend fun refrescar(): Result<Int> {
            refreshCallCount++
            if (shouldFail) {
                return Result.failure(IOException("Network error"))
            }
            _items.value = itemsToReturn
            return Result.success(itemsToReturn.size)
        }

        override suspend fun obtenerDetalle(id: Int): Result<Item> {
            val item = _items.value.find { it.id == id }
            return if (item != null) Result.success(item) else Result.failure(Exception("Not found"))
        }

        override suspend fun alternarFavorito(id: Int) {
            // No-op for fake
        }

        override suspend fun quitarTodosFavoritos() {
            _favoritos.value = emptyList()
        }

        override suspend fun agregarProducto(
            title: String,
            price: Double,
            category: String,
            description: String,
            image: String
        ): Result<Boolean> {
            return Result.success(true)
        }

        override suspend fun cantidadGuardada(): Int = _items.value.size

        override suspend fun cantidadFavoritos(): Int = _favoritos.value.size

        override suspend fun limpiarCache() {
            _items.value = emptyList()
        }
    }
}
