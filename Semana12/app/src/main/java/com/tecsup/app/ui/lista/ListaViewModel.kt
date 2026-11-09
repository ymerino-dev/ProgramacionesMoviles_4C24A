package com.tecsup.app.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.app.data.repository.ItemRepository
import com.tecsup.app.domain.model.Item
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListaViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {

    // Expone los productos en tiempo real para la pantalla
    val items: StateFlow<List<Item>> = itemRepository.items
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Al iniciar, refrescamos los datos desde la API hacia la base de datos local
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            itemRepository.refrescar()
        }
    }
}