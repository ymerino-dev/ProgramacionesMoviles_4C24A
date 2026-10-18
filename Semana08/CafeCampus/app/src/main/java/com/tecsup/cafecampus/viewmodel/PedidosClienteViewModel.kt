package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PedidosClienteViewModel(private val repo: CafeRepository) : ViewModel() {

    // Guardamos el ID del cliente logueado para filtrar sus compras
    private val _clienteId = MutableStateFlow(0)

    // 1. Historial de pedidos del cliente en tiempo real
    val misPedidos: StateFlow<List<PedidoConDetalle>> = _clienteId
        .flatMapLatest { id -> repo.observarPedidosCliente(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setClienteId(id: Int) {
        _clienteId.value = id
    }

    // 2. Observa un solo pedido para la pantalla de seguimiento
    fun pedido(id: Int): Flow<PedidoConDetalle?> {
        return repo.observarPedido(id)
    }

    // 3. Función para confirmar la compra y guardarla en Room
    fun confirmar(
        clienteId: Int,
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String,
        onExito: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val nuevoPedidoId = repo.crearPedido(clienteId, items, horaRecojo, pago, nota)
            onExito(nuevoPedidoId)
        }
    }
}