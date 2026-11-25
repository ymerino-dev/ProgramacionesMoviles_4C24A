package com.merino.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merino.avisoscampus.data.AvisosRepository
import com.merino.avisoscampus.model.Aviso
import com.merino.avisoscampus.model.Origen
import com.merino.avisoscampus.model.PermisoUi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {

    val avisos: StateFlow<List<Aviso>> = repo.avisos

    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()

    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun permisoRespondido(concedido: Boolean) {
        _permiso.value = if (concedido) PermisoUi.Concedido else PermisoUi.Denegado
        if (concedido) {
            _mensaje.value = null
        }
    }

    fun avisarAhora() {
        val aviso = repo.registrar(
            titulo = "Aviso del campus",
            texto = "Revisa los horarios actualizados en el portal.",
            origen = Origen.BOTON
        )
        viewModelScope.launch {
            _eventos.emit(aviso)
        }
    }

    // TODO Paso 5: faltan, recordarEn(segundos) y cancelarRecordatorio()

    // TODO Paso 6: escuchandoAvion, cambiarEscucha(valor) y alCambiarModoAvion(activo)

    // TODO Paso 8: fun noSePudoMostrar()
}