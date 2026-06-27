package com.example.helptataapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helptataapp.model.LoginRequest
import com.example.helptataapp.model.LoginResponse
import com.example.helptataapp.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle    : LoginState()
    object Loading : LoginState()
    data class Success(val response: LoginResponse) : LoginState()
    data class Error(val mensaje: String)           : LoginState()
}

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val res = RetrofitClient.api.login(LoginRequest(email, password))
                if (res.isSuccessful && res.body() != null) {
                    _state.value = LoginState.Success(res.body()!!)
                } else {
                    _state.value = LoginState.Error("Correo o contraseña incorrectos.")
                }
            } catch (e: Exception) {
                _state.value = LoginState.Error("No se pudo conectar al servidor.")
            }
        }
    }
}
