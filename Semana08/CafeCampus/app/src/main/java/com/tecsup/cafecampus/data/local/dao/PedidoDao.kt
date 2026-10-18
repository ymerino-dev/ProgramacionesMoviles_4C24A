package com.tecsup.cafecampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Pedido
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {
    // --- 1. ESCRITURA (suspend) ---
    @Insert
    suspend fun insertarPedido(p: Pedido): Long // Devuelve el número de boleta creado

    @Insert
    suspend fun insertarDetalles(lista: List<DetallePedido>)

    @Query("UPDATE pedidos SET estado = :estado WHERE id = :id")
    suspend fun cambiarEstado(id: Int, estado: String)

    // --- 2. TRANSACCIÓN PARA CREAR PEDIDO ---
    // Hace todo el trabajo sucio: crea la boleta, obtiene el ID, se lo pega a los
    // productos y luego guarda los productos.
    @Transaction
    suspend fun crearPedido(pedido: Pedido, detalles: List<DetallePedido>): Long {
        val pedidoId = insertarPedido(pedido)
        val detallesConPedidoId = detalles.map { it.copy(pedidoId = pedidoId.toInt()) }
        insertarDetalles(detallesConPedidoId)
        return pedidoId
    }

    // --- 3. LECTURA  ---
    @Transaction
    @Query("SELECT * FROM pedidos WHERE clienteId = :clienteId ORDER BY fecha DESC")
    fun observarPorCliente(clienteId: Int): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE estado = :estado ORDER BY fecha DESC")
    fun observarPorEstado(estado: String): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :id")
    fun observarUno(id: Int): Flow<PedidoConDetalle?>

    // --- 4. PANEL DEL ADMINISTRADOR ---
    // Suma todo el dinero de hoy, ignorando los cancelados.
    @Query("SELECT COALESCE(SUM(total), 0.0) FROM pedidos WHERE fecha >= :inicio AND estado != 'CANCELADO'")
    fun ventasDesde(inicio: Long): Flow<Double>

    @Query("SELECT COUNT(*) FROM pedidos WHERE estado = :estado")
    fun cantidadPorEstado(estado: String): Flow<Int>
}