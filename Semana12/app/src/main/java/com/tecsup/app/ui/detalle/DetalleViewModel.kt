package com.tecsup.app.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.app.data.repository.ItemRepository
import com.tecsup.app.domain.model.Item
import com.tecsup.app.notification.Notificador
import com.tecsup.app.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val itemId: Int,
    private val repository: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val uiState: StateFlow<UiState<Item>> = _uiState.asStateFlow()

    init {
        cargarDetalle()
    }

    fun cargarDetalle() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            val resultado = repository.obtenerDetalle(itemId)
            resultado.onSuccess { item ->
                _uiState.value = UiState.Exito(item)
            }.onFailure { e ->
                _uiState.value = UiState.Error(e.message ?: "Error al cargar el detalle")
            }
        }
    }

    fun alternarFavorito() {
        viewModelScope.launch {
            repository.alternarFavorito(itemId)
            val currentState = _uiState.value
            if (currentState is UiState.Exito) {
                val item = currentState.datos
                val nuevoEstado = !item.esFavorito
                _uiState.value = UiState.Exito(item.copy(esFavorito = nuevoEstado))
                if (nuevoEstado) {
                    notificador.mostrarNotificacion("Producto Favorito", "Se agregó '${item.title}' a tus favoritos desde el detalle")
                } else {
                    notificador.mostrarNotificacion("Producto Favorito", "Se eliminó '${item.title}' de tus favoritos")
                }
            }
        }
    }
}
