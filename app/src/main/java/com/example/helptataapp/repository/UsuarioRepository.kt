package com.example.helptataapp.repository

import com.example.helptataapp.model.UsuarioRequest
import com.example.helptataapp.remote.RetrofitClient

class UsuarioRepository {

    suspend fun registrarUsuario(
        usuario: UsuarioRequest
    ) = RetrofitClient.api
        .registrarUsuario(usuario)

}