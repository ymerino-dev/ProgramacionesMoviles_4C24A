package com.tecsup.app.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.app.data.repository.ItemRepository
import com.tecsup.app.notification.Notificador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val itemRepository: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _totalGuardados = MutableStateFlow(0)
    val totalGuardados: StateFlow<Int> = _totalGuardados

    private val _totalFavoritos = MutableStateFlow(0)
    val totalFavoritos: StateFlow<Int> = _totalFavoritos

    init {
        actualizarTotales()
    }

    fun actualizarTotales() {
        viewModelScope.launch {
            _totalGuardados.value = itemRepository.cantidadGuardada()
            _totalFavoritos.value = itemRepository.cantidadFavoritos()
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            val result = itemRepository.refrescar()
            actualizarTotales()
            result.onSuccess { n ->
                notificador.mostrarNotificacion("Sincronización Exitosa", "Se guardaron $n elementos")
            }
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            itemRepository.limpiarCache()
            actualizarTotales()
        }
    }
}
