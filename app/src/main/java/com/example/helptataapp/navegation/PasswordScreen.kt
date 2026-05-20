package com.example.helptataapp.navegation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.helptataapp.model.UsuarioRequest
import com.example.helptataapp.viewmodel.RegisterViewModel


@Composable
fun PasswordScreen(

    viewModel: RegisterViewModel

) {

    val context = LocalContext.current

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement =
            Arrangement.Center

    ) {

        Text(

            text = "Cree una contraseña",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium

        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        OutlinedTextField(

            value =
                viewModel
                    .password_usuario
                    .value,

            onValueChange = {

                viewModel
                    .password_usuario
                    .value = it

            },

            label = {
                Text("Contraseña")
            },

            visualTransformation =
                PasswordVisualTransformation(),

            modifier =
                Modifier.fillMaxWidth()

        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(

            onClick = {

                // Aqui se crea el usuario final, con todos los datos recolectados anteriormente, para
                //posteriormente enviarlo a la API.

                val usuario = UsuarioRequest(

                    run_usuario =
                        viewModel
                            .run_usuario
                            .value,

                    dvrun_usuario =
                        viewModel
                            .dvrun_usuario
                            .value,

                    pnombre_usuario =
                        viewModel
                            .pnombre_usuario
                            .value,

                    snombre_usuario =
                        viewModel
                            .snombre_usuario
                            .value,

                    papellido_usuario =
                        viewModel
                            .papellido_usuario
                            .value,

                    sapellido_usuario =
                        viewModel
                            .sapellido_usuario
                            .value,

                    fecha_nac_usuario =
                        viewModel
                            .fecha_nac_usuario
                            .value,

                    telefono_usuario =
                        viewModel
                            .telefono_usuario
                            .value
                            .toInt(),

                    password_usuario =
                        viewModel
                            .password_usuario
                            .value

                )

                // Aqui se envia a la API y registra un nuevo usuario :3

                viewModel
                    .registrarUsuario(
                        usuario
                    )

                Toast.makeText(

                    context,

                    "Registrando usuario",

                    Toast.LENGTH_LONG

                ).show()

            },

            modifier =
                Modifier.fillMaxWidth()

        ) {

            Text("Finalizar registro")

        }

    }

}