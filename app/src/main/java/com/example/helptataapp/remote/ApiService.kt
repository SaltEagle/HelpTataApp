package com.example.helptataapp.remote

import com.example.helptataapp.model.UsuarioRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/usuarios")
    suspend fun registrarUsuario(


        @Body usuario: UsuarioRequest


    ): Response<UsuarioRequest>

}