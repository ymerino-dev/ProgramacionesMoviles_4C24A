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
                notificador.mostrarNotificacion("Datos actualizados", "Se guardaron $n productos")
            }.onFailure { e ->
                notificador.mostrarNotificacion("Error de Sincronización", e.message ?: "Falló la sincronización")
            }
        }
    }

    fun agregarProducto(title: String, price: Double, category: String, description: String, image: String) {
        viewModelScope.launch {
            val result = itemRepository.agregarProducto(title, price, category, description, image)
            result.onSuccess {
                actualizarTotales()
                notificador.mostrarNotificacion("Producto Creado", "Se agregó '$title' al catálogo")
            }
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            itemRepository.limpiarCache()
            actualizarTotales()
            notificador.mostrarNotificacion("Caché Limpiada", "Se han borrado los productos locales")
        }
    }
}
