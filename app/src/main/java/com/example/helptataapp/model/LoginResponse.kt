package com.example.helptataapp.model

data class LoginResponse(
    val id_usuario: Int,
    val pnombre_usuario: String,
    val papellido_usuario: String,
    val email: String,
    val rol: String,
    val token: String
)
