package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(private val repo: CafeRepository) : ViewModel() {

    // Catálogo de productos en tiempo real
    val productos: StateFlow<List<Producto>> = repo.observarProductos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Búsqueda y Filtro de categorías
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow("Todo")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada.asStateFlow()

    // Productos filtrados dinámicamente mediante combine
    val productosFiltrados: StateFlow<List<Producto>> = combine(
        repo.observarProductos(),
        _searchQuery,
        _categoriaSeleccionada
    ) { lista, query, cat ->
        lista.filter { p ->
            val coincideCategoria = cat == "Todo" || p.categoria.equals(cat, ignoreCase = true)
            val coincideBusqueda = query.isBlank() || p.nombre.contains(query, ignoreCase = true)
            coincideCategoria && coincideBusqueda
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategoriaChange(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }

    // Carrito de compras (memoria temporal)
    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()

    // Carga los productos de un pedido anterior al carrito
    fun repetirPedido(pedidoConDetalle: PedidoConDetalle) {
        val listaActual = _carrito.value.toMutableList()
        pedidoConDetalle.detalles.forEach { detalle ->
            val producto = productos.value.find { it.id == detalle.productoId }
                ?: Producto(
                    id = detalle.productoId,
                    nombre = detalle.nombreProducto,
                    descripcion = "",
                    categoria = "General",
                    precio = detalle.precioUnitario,
                    disponible = true,
                    icono = "plato"
                )

            val index = listaActual.indexOfFirst { it.producto.id == producto.id }
            if (index != -1) {
                val itemExistente = listaActual[index]
                listaActual[index] = itemExistente.copy(
                    cantidad = itemExistente.cantidad + detalle.cantidad
                )
            } else {
                listaActual.add(ItemCarrito(producto, detalle.cantidad, ""))
            }
        }
        _carrito.value = listaActual
    }

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