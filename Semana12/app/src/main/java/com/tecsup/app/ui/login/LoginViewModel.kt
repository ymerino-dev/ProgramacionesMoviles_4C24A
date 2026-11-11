package com.tecsup.app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.app.data.auth.AuthRepository
import com.tecsup.app.domain.model.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Result<Sesion>?>(null)
    val loginState: StateFlow<Result<Sesion>?> = _loginState

    fun iniciarSesion(email: String, pass: String) {
        viewModelScope.launch {
            val resultado = authRepository.iniciarSesion(email, pass)
            _loginState.value = resultado
        }
    }

    fun resetLoginState() {
        _loginState.value = null
    }
}
