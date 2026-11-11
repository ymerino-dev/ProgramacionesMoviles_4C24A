package com.tecsup.app.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.app.data.repository.ItemRepository
import com.tecsup.app.domain.model.Item
import com.tecsup.app.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import retrofit2.HttpException

class ListaViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    // Exponemos la lista de favoritos directamente desde el repositorio como StateFlow para la UI
    val favoritos: StateFlow<List<Item>> = itemRepository.favoritos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            try {
                itemRepository.items.collect { lista ->
                    if (lista.isNotEmpty() || refrescoTerminado) {
                        _estado.value = UiState.Exito(lista)
                    }
                }
            } catch (e: Exception) {
                println("ListaViewModel Error al recolectar items de Room: ${e.message}")
            }
        }
        cargarProductos()
    }

    fun cargarProductos() {
        _aviso.value = null
        if (_estado.value !is UiState.Exito) {
            _estado.value = UiState.Cargando
        }
        viewModelScope.launch {
            try {
                val resultado = itemRepository.refrescar()
                refrescoTerminado = true
                if (resultado.isSuccess) {
                    _aviso.value = null
                } else {
                    val e = resultado.exceptionOrNull() ?: Exception("Error desconocido")
                    println("ListaViewModel Refrescar Failure: ${e.message}")
                    val cantidad = itemRepository.cantidadGuardada()
                    if (cantidad == 0) {
                        _estado.value = UiState.Error(mensajeDe(e))
                    } else {
                        _aviso.value = "Sin conexión: mostrando datos guardados"
                    }
                }
            } catch (e: Exception) {
                println("ListaViewModel Exception: ${e.message}")
                refrescoTerminado = true
                val cantidad = itemRepository.cantidadGuardada()
                if (cantidad == 0) {
                    _estado.value = UiState.Error(mensajeDe(e))
                } else {
                    _aviso.value = "Sin conexión: mostrando datos guardados"
                }
            }
        }
    }

    // Función para alternar el estado de favorito de un producto desde la UI
    fun alternarFavorito(id: Int) {
        viewModelScope.launch {
            try {
                itemRepository.alternarFavorito(id)
            } catch (e: Exception) {
                println("ListaViewModel Error al alternar favorito: ${e.message}")
            }
        }
    }

    private fun mensajeDe(e: Throwable): String {
        return when (e) {
            is IOException -> "No hay conexión a internet"
            is HttpException -> "El servidor respondió con error ${e.code()}"
            else -> "Ocurrió un error inesperado: ${e.localizedMessage}"
        }
    }
}