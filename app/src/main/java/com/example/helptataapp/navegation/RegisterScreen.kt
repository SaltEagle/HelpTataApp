package com.example.helptataapp.navegation

//Este codigo ya no se utiliza, es para tenerlo por si se rompe lo demas.

//import android.util.Log
//import android.widget.Toast
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.helptataapp.model.UsuarioRequest
//import com.example.helptataapp.viewmodel.RegisterViewModel
//
//@Composable
//fun RegisterScreen(
//
//    viewModel: RegisterViewModel = viewModel()
//
//) {
//
//    val context = LocalContext.current
//
//    val mensaje by
//    viewModel.mensaje.collectAsState()
//
//    var run by remember { mutableStateOf("") }
//    var dvRun by remember { mutableStateOf("") }
//    var nombre by remember { mutableStateOf("") }
//    var apellido by remember { mutableStateOf("") }
//    var fechaNacimiento by remember {
//        mutableStateOf("")
//    }
//
//    var telefono by remember {
//        mutableStateOf("")
//    }
//
//    var password by remember {
//        mutableStateOf("")
//    }
//
//    LaunchedEffect(mensaje) {
//
//        if (mensaje.isNotEmpty()) {
//
//            Toast.makeText(
//                context,
//                mensaje,
//                Toast.LENGTH_LONG
//            ).show()
//
//        }
//
//    }
//
//    Column(
//
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(20.dp),
//
//        verticalArrangement =
//            Arrangement.spacedBy(10.dp)
//
//    ) {
//
//        OutlinedTextField(
//            value = run,
//            onValueChange = { run = it },
//            label = { Text("RUN") }
//        )
//
//        OutlinedTextField(
//            value = dvRun,
//            onValueChange = { dvRun = it },
//            label = { Text("DV") }
//        )
//
//        OutlinedTextField(
//            value = nombre,
//            onValueChange = { nombre = it },
//            label = { Text("Nombre") }
//        )
//
//        OutlinedTextField(
//            value = apellido,
//            onValueChange = { apellido = it },
//            label = { Text("Apellido") }
//        )
//
//        OutlinedTextField(
//            value = fechaNacimiento,
//            onValueChange = {
//                fechaNacimiento = it
//            },
//            label = {
//                Text("2000-01-15")
//            }
//        )
//
//        OutlinedTextField(
//            value = telefono,
//            onValueChange = {
//                telefono = it
//            },
//            label = {
//                Text("Teléfono")
//            }
//        )
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = {
//                password = it
//            },
//
//            label = {
//                Text("Contraseña")
//            },
//
//            visualTransformation =
//                PasswordVisualTransformation()
//        )
//
//        Button(
//
//            onClick = {
//
//                val usuario = UsuarioRequest(
//
//                    run_usuario = run,
//
//                    dvrun_usuario = dvRun,
//
//                    pnombre_usuario = nombre,
//
//                    snombre_usuario = null,
//
//                    papellido_usuario = apellido,
//
//                    sapellido_usuario = null,
//
//                    fecha_nac_usuario =
//                        fechaNacimiento,
//
//                    telefono_usuario =
//                        telefono.toInt(),
//
//                    password_usuario =
//                        password,
//
//
//
//                )
//
//                viewModel.registrarUsuario(
//                    usuario
//
//                )
//
//                Log.d("API_TEST", usuario.toString())
//
//            },
//
//            modifier =
//                Modifier.fillMaxWidth()
//
//        ) {
//
//            Text("Registrarse")
//
//        }
//
//    }
//
//}