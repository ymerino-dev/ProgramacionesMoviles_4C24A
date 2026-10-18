package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(private val repo: CafeRepository) : ViewModel() {

    // Catálogo de productos en tiempo real
    val productos: StateFlow<List<Producto>> = repo.observarProductos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Carrito de compras (memoria temporal)
    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()

    // Funciones requeridas por el contrato
    fun agregar(producto: Producto, cantidad: Int, nota: String) {
        val listaActual = _carrito.value.toMutableList()
        val index = listaActual.indexOfFirst { it.producto.id == producto.id }

        if (index != -1) {
            // Si existe, suma la cantidad
            val itemExistente = listaActual[index]
            listaActual[index] = itemExistente.copy(
                cantidad = itemExistente.cantidad + cantidad,
                nota = nota
            )
        } else {
            // Si es nuevo, lo agrega a la lista
            listaActual.add(ItemCarrito(producto, cantidad, nota))
        }
        _carrito.value = listaActual
    }

    fun quitar(productoId: Int) {
        _carrito.value = _carrito.value.filter { it.producto.id != productoId }
    }

    fun vaciar() {
        _carrito.value = emptyList()
    }

    fun total(): Double {
        return _carrito.value.sumOf { it.producto.precio * it.cantidad }
    }
}