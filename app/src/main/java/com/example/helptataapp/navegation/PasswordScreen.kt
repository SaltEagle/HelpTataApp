package com.example.helptataapp.navegation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.helptataapp.model.UsuarioRequest
import com.example.helptataapp.ui.components.*
import com.example.helptataapp.ui.theme.*
import com.example.helptataapp.viewmodel.RegisterViewModel

@Composable
fun PasswordScreen(
    viewModel: RegisterViewModel
) {
    val context = LocalContext.current
    var error         by remember { mutableStateOf("") }
    var errorConfirm  by remember { mutableStateOf("") }
    var confirmPass   by remember { mutableStateOf("") }

    HtFormBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 36.dp)
        ) {
            HtStepHeader(
                currentStep = 7,
                totalSteps  = 7,
                stepTitle   = "Crea tu contraseña"
            )

            Text(
                text  = "Elige una contraseña segura. Debe tener al menos 6 caracteres. Puedes usar letras, números o símbolos.",
                style = MaterialTheme.typography.bodyMedium,
                color = HT_TextMid
            )

            Spacer(Modifier.height(24.dp))

            // Contraseña
            HtTextField(
                value         = viewModel.password_usuario.value,
                onValueChange = {
                    viewModel.password_usuario.value = it
                    if (error.isNotEmpty()) error = ""
                },
                label                = "Contraseña",
                errorMessage         = error,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions      = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )

            Spacer(Modifier.height(16.dp))

            // Confirmar contraseña
            HtTextField(
                value         = confirmPass,
                onValueChange = {
                    confirmPass = it
                    if (errorConfirm.isNotEmpty()) errorConfirm = ""
                },
                label                = "Repite la contraseña",
                errorMessage         = errorConfirm,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions      = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )

            Spacer(Modifier.height(32.dp))

            HtPrimaryButton(
                text    = "Finalizar registro",
                onClick = {
                    var ok = true
                    if (viewModel.password_usuario.value.isBlank()) {
                        error = "Por favor crea una contraseña."; ok = false
                    } else if (viewModel.password_usuario.value.length < 6) {
                        error = "La contraseña debe tener al menos 6 caracteres."; ok = false
                    }
                    if (confirmPass != viewModel.password_usuario.value) {
                        errorConfirm = "Las contraseñas no coinciden."; ok = false
                    }

                    if (ok) {
                        val usuario = UsuarioRequest(
                            run_usuario       = viewModel.run_usuario.value,
                            dvrun_usuario     = viewModel.dvrun_usuario.value,
                            pnombre_usuario   = viewModel.pnombre_usuario.value,
                            snombre_usuario   = viewModel.snombre_usuario.value,
                            papellido_usuario = viewModel.papellido_usuario.value,
                            sapellido_usuario = viewModel.sapellido_usuario.value,
                            fecha_nac_usuario = viewModel.fecha_nac_usuario.value,
                            telefono_usuario  = viewModel.telefono_usuario.value.toInt(),
                            password_usuario  = viewModel.password_usuario.value
                        )
                        viewModel.registrarUsuario(usuario)
                        Toast.makeText(context, "Registrando usuario…", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }
}
