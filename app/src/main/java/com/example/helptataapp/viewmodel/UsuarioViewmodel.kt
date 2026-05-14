package com.example.helptataapp.viewmodel
import androidx.lifecycle.ViewModel
import com.example.helptataapp.model.*
import kotlinx.coroutines.flow.*
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    // ESTADO INTERNO MUTABLE
    private val _estado = MutableStateFlow(Usuario()) //
    val estado: StateFlow<Usuario> = _estado

    // Ru
    fun onrunChange(valor: Int) {
        _estado.update { it.copy(run = valor, errores = it.errores.copy(run = null)) }
    }

    fun ondvrunChange(valor: Int) {
        _estado.update { it.copy(dvrun = valor, errores = it.errores.copy(dvrun = null)) }
    }

    // Nombres
    fun onpnombrechange(valor: String) {
        _estado.update { it.copy(pnombre = valor, errores = it.errores.copy(pnombre = null)) }
    }

    fun onsnombrechange(valor: String) {
        _estado.update { it.copy(snombre = valor, errores = it.errores.copy(snombre = null)) }
    }

    // Apellidos
    fun onpapellidochange(valor: String){
        _estado.update { it.copy(papellido = valor, errores = it.errores.copy(papellido = null)) }
    }

    fun onsapellidochange(valor: String){
        _estado.update{ it.copy(sapellido = valor, errores = it.errores.copy(papellido = null))}
    }

    // Correo
    fun oncorreochange(valor: String){
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    // Fecha
    fun onfechaonchange(valor : Date){
        _estado.update { it.copy(fechanac = valor, errores = it.errores.copy(fechanac = null)) }
    }

    // Contra el juego del nes
    fun oncontrachange(valor: String){
        _estado.update { it.copy(contra = valor, errores = it.errores.copy(contra = null)) }
    }






}