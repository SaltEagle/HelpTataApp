package com.example.helptataapp.model

import java.util.Date


// Se crea la base del usuario

data class Usuario (

    // Rut

    val run: Int = 0,
    val dvrun: Int = 0,

    // Nombres
    val pnombre: String = "",
    val snombre: String = "",

    // Apellidos
    val papellido: String = "",
    val sapellido: String = "",

    // Correo
    val correo: String = "",

    // Fecha
    val fechanac: Date = Date(),

    // Contra el juegito de nes
    val contra: String = "",

    // Otros (Fake Loading, y el analisis de errores)
    val isLoading: Boolean = true,
    val errores: UsuarioProblemos = UsuarioProblemos()
)
