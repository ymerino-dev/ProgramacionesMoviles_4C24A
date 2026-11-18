package com.merino.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merino.campusgps.data.GpsApagadoException
import com.merino.campusgps.data.LugaresRepository
import com.merino.campusgps.data.PreferenciaRepository
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
    private val lugaresRepo: LugaresRepository,
    private val preferenciaRepo: PreferenciaRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private var categoriaActual = "Todas"
    private var radioActual = 200.0
    private var ultimaPosicion: Posicion? = null

    private var listaCompletaLugares: List<LugarConDistancia> = lugaresRepo.obtenerLugares().map {
        LugarConDistancia(it, null, true)
    }

    private val _lugares = MutableStateFlow<List<LugarConDistancia>>(listaCompletaLugares)
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    val masCercano: StateFlow<Lugar?> = _lugares
        .map { lista ->
            lista.firstOrNull { it.metros != null }?.lugar
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        // COMENTARIO [DataStore]: Recupera el último lugar seleccionado guardado en DataStore al iniciar la app
        viewModelScope.launch {
            preferenciaRepo.ultimoLugarId.collect { idGuardado ->
                if (idGuardado != null && _seleccionado.value == null) {
                    _seleccionado.value = idGuardado
                }
            }
        }
    }

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
                .onSuccess { pos ->
                    ultimaPosicion = pos
                    actualizarDistanciasYEstado(pos)
                }
                .onFailure { _estado.value = errorDe(it) }
        }
    }

    // COMENTARIO [Cálculo de Distancia y Radio]: Calcula la distancia en metros entre el usuario y cada lugar,
    // evaluando si se encuentra dentro del radio actual seleccionado (100m, 200m, 500m).
    private fun actualizarDistanciasYEstado(posicion: Posicion) {
        listaCompletaLugares = lugaresRepo.obtenerLugares()
            .map { lugar ->
                val dist = calcularDistanciaMetros(posicion, lugar.latitud, lugar.longitud)
                val dentro = dist <= radioActual
                LugarConDistancia(lugar, dist, dentroDelRadio = dentro)
            }
            .sortedBy { it.metros ?: Float.MAX_VALUE }

        aplicarFiltros(posicion)
    }

    // COMENTARIO [Filtrado por Categoría]: Filtra la lista completa de lugares según la categoría elegida
    private fun aplicarFiltros(posicion: Posicion?) {
        val filtrados = if (categoriaActual.equals("Todas", ignoreCase = true)) {
            listaCompletaLugares
        } else {
            listaCompletaLugares.filter {
                it.lugar.categoria.equals(categoriaActual, ignoreCase = true)
            }
        }

        _lugares.value = filtrados

        if (posicion != null) {
            _estado.value = UbicacionUi.Lista(
                posicion = posicion,
                categoriaSeleccionada = categoriaActual,
                lugaresFiltrados = filtrados,
                radioMetros = radioActual
            )
        }
    }

    fun seleccionarCategoria(categoria: String) {
        categoriaActual = categoria
        aplicarFiltros(ultimaPosicion)
    }

    fun cambiarRadio(nuevoRadio: Double) {
        radioActual = nuevoRadio
        val pos = ultimaPosicion
        if (pos != null) {
            actualizarDistanciasYEstado(pos)
        } else {
            aplicarFiltros(null)
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            mensaje = "Sin permiso de ubicación no puedo mostrar dónde estás",
            tipo = TipoError.SIN_PERMISO
        )
    }

    // COMENTARIO [Persistencia DataStore]: Selecciona/desselecciona un lugar y persiste el ID en DataStore
    fun seleccionar(id: Int?) {
        val nuevoId = if (_seleccionado.value == id) null else id
        _seleccionado.value = nuevoId
        viewModelScope.launch {
            preferenciaRepo.guardarUltimoLugarId(nuevoId)
        }
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
