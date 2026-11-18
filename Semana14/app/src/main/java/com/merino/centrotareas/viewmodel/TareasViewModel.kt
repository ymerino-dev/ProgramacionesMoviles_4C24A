package com.merino.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merino.centrotareas.data.TareasRepository
import com.merino.centrotareas.model.CargaUi
import com.merino.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {

    // ---- Historial de ejecuciones ----
    private val _historial = MutableStateFlow<List<String>>(emptyList())
    val historial: StateFlow<List<String>> = _historial.asStateFlow()

    private fun agregarAlHistorial(registro: String) {
        val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        _historial.value = listOf("[$hora] $registro") + _historial.value
    }

    fun limpiarHistorial() {
        _historial.value = emptyList()
    }

    // ---- Tarea con progreso (pasos 1 a 4) ----
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    private var trabajo: Job? = null

    fun tareaLarga() {
        if (_corriendo.value) return

        trabajo = viewModelScope.launch {
            _corriendo.value = true
            _progreso.value = 0f
            _mensaje.value = "Trabajando..."
            Log.d("HILO", "Antes: ${Thread.currentThread().name}")

            try {
                repo.ejecutarTareaConProgreso().collect { valor ->
                    _progreso.value = valor
                }
                Log.d("HILO", "Despues: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
                agregarAlHistorial("Tarea con progreso: Finalizada (100%)")
            } catch (e: CancellationException) {
                _mensaje.value = "Cancelado"
                agregarAlHistorial("Tarea con progreso: Cancelada")
                throw e
            } catch (e: Exception) {
                _mensaje.value = "Error: ${e.message}"
                agregarAlHistorial("Tarea con progreso: Error (${e.message})")
            } finally {
                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        trabajo?.cancel()
        _mensaje.value = "Cancelado"
    }

    // ---- Contar primos (paso 5) ----
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private var trabajoPrimos: Job? = null

    fun contar(hasta: Int) {
        if (hasta < 2) return
        trabajoPrimos?.cancel()

        trabajoPrimos = viewModelScope.launch {
            _primos.value = PrimosUi.Calculando(0f)
            val inicio = System.currentTimeMillis()
            try {
                val total = repo.contarPrimos(hasta) { progreso ->
                    _primos.value = PrimosUi.Calculando(progreso)
                }
                val duracion = System.currentTimeMillis() - inicio
                _primos.value = PrimosUi.Listo(cantidad = total, milisegundos = duracion)
                agregarAlHistorial("Contar primos (hasta $hasta): $total primos en $duracion ms")
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                agregarAlHistorial("Contar primos (hasta $hasta): Cancelado")
                throw e
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    // ---- Dos cargas (pasos 6 y 7) ----
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    fun cargarUnaPorUna() {
        if (_carga.value is CargaUi.Cargando) return

        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                val perfil = repo.cargarPerfil()
                val pedidos = repo.cargarPedidos(_simularError.value)
                val duracion = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito("Una por una", perfil, pedidos.size, duracion)
                agregarAlHistorial("Cargas (Una por una): Éxito en $duracion ms")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _carga.value = CargaUi.Error("No se pudo cargar: ${e.message}")
                agregarAlHistorial("Cargas (Una por una): Error (${e.message})")
            }
        }
    }

    fun cargarALaVez() {
        if (_carga.value is CargaUi.Cargando) return

        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                val (perfil, pedidos) = coroutineScope {
                    val a = async { repo.cargarPerfil() }
                    val b = async { repo.cargarPedidos(_simularError.value) }
                    Pair(a.await(), b.await())
                }
                val duracion = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito("A la vez", perfil, pedidos.size, duracion)
                agregarAlHistorial("Cargas (A la vez): Éxito en $duracion ms")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _carga.value = CargaUi.Error("No se pudo cargar: ${e.message}")
                agregarAlHistorial("Cargas (A la vez): Error (${e.message})")
            }
        }
    }
}