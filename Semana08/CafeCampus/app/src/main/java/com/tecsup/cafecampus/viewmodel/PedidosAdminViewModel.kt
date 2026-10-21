package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.EstadoPedido
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PedidosAdminViewModel(private val repo: CafeRepository) : ViewModel() {

    // El filtro de los "Chips" (Por defecto arranca viendo los pedidos RECIBIDOS)
    private val _estadoSeleccionado = MutableStateFlow(EstadoPedido.RECIBIDO)
    val estadoSeleccionado: StateFlow<EstadoPedido> = _estadoSeleccionado.asStateFlow()

    // Cambia la lista de pedidos automáticamente si el administrador toca otro chip
    val pedidos: StateFlow<List<PedidoConDetalle>> = _estadoSeleccionado
        .flatMapLatest { estado -> repo.observarPedidosPorEstado(estado) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun seleccionarEstado(estado: EstadoPedido) {
        _estadoSeleccionado.value = estado
    }

    // Lógica de avance: RECIBIDO -> PREPARANDO -> LISTO -> ENTREGADO
    fun avanzar(pedidoId: Int, estadoActual: String) {
        val nuevoEstado = when (estadoActual) {
            EstadoPedido.RECIBIDO.name -> EstadoPedido.PREPARANDO
            EstadoPedido.PREPARANDO.name -> EstadoPedido.LISTO
            EstadoPedido.LISTO.name -> EstadoPedido.ENTREGADO
            else -> return // Si ya está entregado o cancelado, no avanza más
        }

        viewModelScope.launch {
            repo.cambiarEstado(pedidoId, nuevoEstado)
        }
    }

    // El botón rojo para cancelar un pedido
    fun cancelar(pedidoId: Int) {
        viewModelScope.launch {
            repo.cambiarEstado(pedidoId, EstadoPedido.CANCELADO)
        }
    }
}