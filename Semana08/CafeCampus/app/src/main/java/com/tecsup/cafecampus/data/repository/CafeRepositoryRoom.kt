package com.tecsup.cafecampus.data.repository

import com.tecsup.cafecampus.data.local.dao.PedidoDao
import com.tecsup.cafecampus.data.local.dao.PedidosPorHora
import com.tecsup.cafecampus.data.local.dao.ProductoDao
import com.tecsup.cafecampus.data.local.dao.ProductoMasVendido
import com.tecsup.cafecampus.data.local.dao.UsuarioDao
import com.tecsup.cafecampus.data.local.dao.VentasPorMetodoPago
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Pedido
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

class CafeRepositoryRoom(
    private val usuarioDao: UsuarioDao,
    private val productoDao: ProductoDao,
    private val pedidoDao: PedidoDao
) : CafeRepository {

    override suspend fun login(correo: String, clave: String): Usuario? {
        return usuarioDao.login(correo, clave)
    }

    override fun observarProductos(): Flow<List<Producto>> = productoDao.observarTodos()

    override suspend fun guardarProducto(p: Producto) {
        productoDao.insertar(p)
    }

    override suspend fun eliminarProducto(p: Producto) {
        productoDao.eliminar(p)
    }

    override suspend fun cambiarDisponibilidad(id: Int, disponible: Boolean) {
        productoDao.cambiarDisponibilidad(id, disponible)
    }

    override suspend fun crearPedido(
        clienteId: Int, items: List<ItemCarrito>, horaRecojo: String, pago: String, nota: String
    ): Long {
        // 1. Calcula el costo total sumando (precio * cantidad) de cada producto en la bolsa
        val totalCalculado = items.sumOf { it.producto.precio * it.cantidad }

        // 2. Prepara la cabecera del recibo
        val pedido = Pedido(
            clienteId = clienteId,
            estado = EstadoPedido.RECIBIDO.name, // Todo pedido nuevo nace como "RECIBIDO"
            horaRecojo = horaRecojo,
            pago = pago,
            nota = nota,
            total = totalCalculado
        )

        // 3. Convierte los ítems del carrito en Detalles de Pedido (líneas del recibo)
        val detalles = items.map { item ->
            DetallePedido(
                pedidoId = 0, // El bibliotecario (DAO) le asignará el ID real después
                productoId = item.producto.id,
                nombreProducto = item.producto.nombre,
                cantidad = item.cantidad,
                precioUnitario = item.producto.precio
            )
        }

        // 4. Le pasa todo al bibliotecario jefe para que lo guarde en una Transacción
        return pedidoDao.crearPedido(pedido, detalles)
    }

    override fun observarPedidosCliente(clienteId: Int): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarPorCliente(clienteId)
    }

    override fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarPorEstado(estado.name)
    }

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> {
        return pedidoDao.observarUno(id)
    }

    override suspend fun cambiarEstado(pedidoId: Int, estado: EstadoPedido) {
        pedidoDao.cambiarEstado(pedidoId, estado.name)
    }

    override fun observarVentasHoy(inicioDia: Long): Flow<Double> {
        return pedidoDao.ventasDesde(inicioDia)
    }

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> {
        return pedidoDao.cantidadPorEstado(estado.name)
    }

    override fun observarProductoMasVendido(): Flow<ProductoMasVendido?> {
        return pedidoDao.productoMasVendido()
    }

    override fun observarVentasPorMetodoPago(): Flow<List<VentasPorMetodoPago>> {
        return pedidoDao.ventasPorMetodoPago()
    }

    override fun observarPedidosPorHora(): Flow<List<PedidosPorHora>> {
        return pedidoDao.pedidosPorHora()
    }
}