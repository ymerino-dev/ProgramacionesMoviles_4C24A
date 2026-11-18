package com.merino.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merino.campusgps.data.LugaresRepository
import com.merino.campusgps.data.UbicacionRepository
import com.merino.campusgps.model.LugarConDistancia
import com.merino.campusgps.model.Posicion
import com.merino.campusgps.model.UbicacionUi
import com.merino.campusgps.util.calcularDistanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    fun buscar() {
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    // TODO Paso 5: actualizarDistancias(it)
                    actualizarDistancias(it)
                }
                .onFailure { _estado.value = UbicacionUi.Error(mensajeDe(it)) }
        }
    }
    private fun actualizarDistancias(posicion: Posicion) {
        _lugares.value = lugaresRepo.obtenerLugares()
            .map { lugar ->
                val dist = calcularDistanciaMetros(posicion, lugar.latitud, lugar.longitud)
                LugarConDistancia(lugar, dist)
            }
            .sortedBy { it.metros ?: Float.MAX_VALUE }
    }
    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error("Sin permiso de ubicación no puedo mostrar dónde estás")
    }

    // TODO Paso 6: fun seleccionar(id: Int?)
    // TODO Paso 7: val masCercano: StateFlow<Lugar?>

    private fun mensajeDe(e: Throwable): String = when (e) {
        is SecurityException -> "Falta el permiso de ubicación"
        else -> e.message ?: "No se pudo obtener la ubicación"
    }
}