package com.example.helptataapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helptataapp.model.UsuarioRequest
import com.example.helptataapp.repository.UsuarioRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val repository =
        UsuarioRepository()

    private val _mensaje =
        MutableStateFlow("")

    val mensaje: StateFlow<String> =
        _mensaje

    fun registrarUsuario(
        usuario: UsuarioRequest
    ) {

        viewModelScope.launch {

            try {

                val response =
                    repository
                        .registrarUsuario(usuario)

                if (response.isSuccessful) {

                    _mensaje.value =
                        "Usuario registrado"

                } else {

                    _mensaje.value =
                        "Error ${response.code()}"

                }

            } catch (e: Exception) {

                _mensaje.value =
                    e.message ?: "Error desconocido"

            }

        }

    }

}