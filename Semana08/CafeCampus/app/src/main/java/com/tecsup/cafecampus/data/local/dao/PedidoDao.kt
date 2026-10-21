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
abstract class PedidoDao {
    // --- 1. ESCRITURA (suspend) ---
    // A las funciones vacías se les agrega la palabra 'abstract'
    @Insert
    abstract suspend fun insertarPedido(p: Pedido): Long

    @Insert
    abstract suspend fun insertarDetalles(lista: List<DetallePedido>): List<Long>

    @Query("UPDATE pedidos SET estado = :estado WHERE id = :id")
    abstract suspend fun cambiarEstado(id: Int, estado: String): Int

    // --- 2. TRANSACCIÓN PARA CREAR PEDIDO ---
    // La función con código adentro se marca como 'open'
    @Transaction
    open suspend fun crearPedido(pedido: Pedido, detalles: List<DetallePedido>): Long {
        val pedidoId = insertarPedido(pedido)
        val detallesConPedidoId = detalles.map { it.copy(pedidoId = pedidoId.toInt()) }
        insertarDetalles(detallesConPedidoId)
        return pedidoId
    }

    // --- 3. LECTURA (Flow - Observadores) ---
    @Transaction
    @Query("SELECT * FROM pedidos WHERE clienteId = :clienteId ORDER BY fecha DESC")
    abstract fun observarPorCliente(clienteId: Int): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE estado = :estado ORDER BY fecha DESC")
    abstract fun observarPorEstado(estado: String): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :id")
    abstract fun observarUno(id: Int): Flow<PedidoConDetalle?>

    // --- 4. PANEL DEL ADMINISTRADOR ---
    @Query("SELECT COALESCE(SUM(total), 0.0) FROM pedidos WHERE fecha >= :inicio AND estado != 'CANCELADO'")
    abstract fun ventasDesde(inicio: Long): Flow<Double>

    @Query("SELECT COUNT(*) FROM pedidos WHERE estado = :estado")
    abstract fun cantidadPorEstado(estado: String): Flow<Int>
}