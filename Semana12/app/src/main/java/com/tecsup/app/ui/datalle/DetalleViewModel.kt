package com.tecsup.app.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.app.domain.model.Item
import com.tecsup.app.domain.repository.ItemRepository
import com.tecsup.app.ui.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val itemId: Int,
    private val repository: ItemRepository
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

    fun alternarFavorito(itemId: Int) {
        viewModelScope.launch {
            repository.alternarFavorito(itemId)
        }
    }
}