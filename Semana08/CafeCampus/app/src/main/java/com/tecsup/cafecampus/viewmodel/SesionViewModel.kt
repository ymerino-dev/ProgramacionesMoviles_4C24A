package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repository: CafeRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    // 1. Estados que la pantalla (UI) va a observar
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // El modo oscuro se convierte de Flow a StateFlow para la UI
    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // 2. Función principal que se ejecuta al darle clic al botón "Ingresar"
    fun login(correo: String, clave: String, rolSeleccionado: String) {
        _error.value = null // Limpia cualquier error previo

        // Validación 1: Campos vacíos
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Por favor, completa todos los campos"
            return
        }

        // Lanza una corrutina porque hablar con el Repositorio (BD) toma tiempo
        viewModelScope.launch {
            val usuario = repository.login(correo, clave)

            // Validación 2: Credenciales incorrectas
            if (usuario == null) {
                _error.value = "Correo o contraseña incorrectos"
            }
            // Validación 3: El rol no coincide
            else if (usuario.rol != rolSeleccionado) {
                _error.value = "El rol seleccionado no coincide con tu cuenta"
            }
            // Éxito total
            else {
                _usuarioActual.value = usuario
                prefs.guardarPerfil(usuario.rol) // Guarda el rol en el DataStore
            }
        }
    }

    // Función auxiliar para borrar el mensaje de error de la pantalla
    fun limpiarError() {
        _error.value = null
    }
    fun logout() {
        _usuarioActual.value = null
        _error.value = null
        viewModelScope.launch {
            prefs.cerrarSesion()
        }
    }
}