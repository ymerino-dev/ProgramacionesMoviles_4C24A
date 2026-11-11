package com.tecsup.app.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.app.data.repository.ItemRepository
import com.tecsup.app.domain.model.Item
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {
    val favoritos: StateFlow<List<Item>> = itemRepository.favoritos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun alternarFavorito(id: Int) {
        viewModelScope.launch {
            itemRepository.alternarFavorito(id)
        }
    }

    fun quitarTodosFavoritos() {
        viewModelScope.launch {
            itemRepository.quitarTodosFavoritos()
        }
    }
}
