package com.example.helptataapp.remote

import com.example.helptataapp.model.EmailRequest
import com.example.helptataapp.model.LoginRequest
import com.example.helptataapp.model.LoginResponse
import com.example.helptataapp.model.UsuarioRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/usuarios")
    suspend fun registrarUsuario(
        @Body usuario: UsuarioRequest
    ): Response<UsuarioRequest>

    @POST("api/emails")
    suspend fun registrarEmail(
        @Body email: EmailRequest
    ): Response<Unit>

    @POST("api/usuarios/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

}