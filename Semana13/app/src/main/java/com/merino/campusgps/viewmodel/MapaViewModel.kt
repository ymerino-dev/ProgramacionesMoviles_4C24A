package com.merino.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merino.campusgps.data.GpsApagadoException
import com.merino.campusgps.data.LugaresRepository
import com.merino.campusgps.data.UbicacionRepository
import com.merino.campusgps.model.Lugar
import com.merino.campusgps.model.LugarConDistancia
import com.merino.campusgps.model.Posicion
import com.merino.campusgps.model.TipoError
import com.merino.campusgps.model.UbicacionUi
import com.merino.campusgps.util.calcularDistanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    // TODO Paso 7: val masCercano: StateFlow<Lugar?>
    val masCercano: StateFlow<Lugar?> = _lugares
        .map { lista ->
            lista.firstOrNull { it.metros != null }?.lugar
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun buscar() {
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                mensaje = "Este dispositivo no tiene Google Play Services",
                tipo = TipoError.SIN_PLAY_SERVICES
            )
            return
        }

        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    // TODO Paso 5: actualizarDistancias(it)
                    actualizarDistancias(it)
                }
                .onFailure { _estado.value = errorDe(it) }
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
        _estado.value = UbicacionUi.Error(
            mensaje = "Sin permiso de ubicación no puedo mostrar dónde estás",
            tipo = TipoError.SIN_PERMISO
        )
    }

    // TODO Paso 6: fun seleccionar(id: Int?)
    fun seleccionar(id: Int?) {
        _seleccionado.value = if (_seleccionado.value == id) null else id
    }

    private fun errorDe(e: Throwable): UbicacionUi.Error = when (e) {
        is SecurityException -> UbicacionUi.Error(
            mensaje = "Falta el permiso de ubicación",
            tipo = TipoError.SIN_PERMISO
        )
        is GpsApagadoException -> UbicacionUi.Error(
            mensaje = "Activa el GPS e intenta de nuevo",
            tipo = TipoError.GPS_APAGADO
        )
        else -> UbicacionUi.Error(
            mensaje = e.message ?: "No se pudo obtener la ubicación",
            tipo = TipoError.OTRO
        )
    }
}