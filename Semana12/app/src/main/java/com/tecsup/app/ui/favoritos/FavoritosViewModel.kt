package com.tecsup.app.ui.favoritos

import androidx.lifecycle.ViewModel
import com.tecsup.app.domain.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoritosViewModel : ViewModel() {
    private val _favoritos = MutableStateFlow<List<Item>>(emptyList())
    val favoritos: StateFlow<List<Item>> = _favoritos
}