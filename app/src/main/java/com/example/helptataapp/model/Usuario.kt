package com.example.helptataapp.model

import java.util.Date

// En tu archivo de Model (donde está UsuarioUiState)
data class Usuario (

    val run : Int,
    val dvrun : Int,

    val pnombre: String = "",
    val snombre: String = "",

    val papellido: String = "",
    val sapellido: String = "",

    val correo: String = "",

    val fechanac: Date,

    val contra: String = "",

    val isLoading: Boolean = true,

)