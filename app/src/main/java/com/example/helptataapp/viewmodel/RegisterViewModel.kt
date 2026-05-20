package com.example.helptataapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helptataapp.model.UsuarioRequest
import com.example.helptataapp.repository.UsuarioRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val repository =
        UsuarioRepository()

    var run_usuario =
        mutableStateOf("")

    var dvrun_usuario =
        mutableStateOf("")

    var pnombre_usuario =
        mutableStateOf("")

    var snombre_usuario =
        mutableStateOf("")

    var papellido_usuario =
        mutableStateOf("")

    var sapellido_usuario =
        mutableStateOf("")

    var fecha_nac_usuario =
        mutableStateOf("")

    var telefono_usuario =
        mutableStateOf("")

    var password_usuario =
        mutableStateOf("")

    // FUNCIÓN NUEVA

    fun registrarUsuario(
        usuario: UsuarioRequest
    ) {

        viewModelScope.launch {

            try {

                repository
                    .registrarUsuario(
                        usuario
                    )

            } catch (e: Exception) {

                e.printStackTrace()

            }

        }

    }

}