package com.example.helptataapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helptataapp.model.EmailRequest
import com.example.helptataapp.model.LoginRequest
import com.example.helptataapp.model.UsuarioRequest
import com.example.helptataapp.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
    object Idle     : RegisterState()
    object Loading  : RegisterState()
    data class SuccessWithToken(val token: String) : RegisterState()
    data class Error(val mensaje: String) : RegisterState()
}

class RegisterViewModel : ViewModel() {

    var run_usuario       = mutableStateOf("")
    var dvrun_usuario     = mutableStateOf("")
    var pnombre_usuario   = mutableStateOf("")
    var snombre_usuario   = mutableStateOf("")
    var papellido_usuario = mutableStateOf("")
    var sapellido_usuario = mutableStateOf("")
    var fecha_nac_usuario = mutableStateOf("")
    var telefono_usuario  = mutableStateOf("")
    var correo_usuario    = mutableStateOf("")
    var password_usuario  = mutableStateOf("")

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val state: StateFlow<RegisterState> = _state

    fun registrarUsuario(usuario: UsuarioRequest, email: String) {
        viewModelScope.launch {
            _state.value = RegisterState.Loading
            try {
                val resUsuario = RetrofitClient.api.registrarUsuario(usuario)
                if (!resUsuario.isSuccessful) {
                    _state.value = RegisterState.Error("No se pudo crear la cuenta. Intenta nuevamente.")
                    return@launch
                }
                RetrofitClient.api.registrarEmail(EmailRequest(email))

                // Auto-login: obtener token con las credenciales recién creadas
                val resLogin = RetrofitClient.api.login(LoginRequest(email, usuario.password_usuario))
                if (resLogin.isSuccessful && resLogin.body() != null) {
                    _state.value = RegisterState.SuccessWithToken(resLogin.body()!!.token)
                } else {
                    // Registro exitoso pero login falló — ir a login manual
                    _state.value = RegisterState.SuccessWithToken("")
                }
            } catch (e: Exception) {
                _state.value = RegisterState.Error("No se pudo conectar al servidor.")
            }
        }
    }
}
