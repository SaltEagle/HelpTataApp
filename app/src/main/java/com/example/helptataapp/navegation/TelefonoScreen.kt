package com.example.helptataapp.navegation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.helptataapp.ui.components.*
import com.example.helptataapp.ui.theme.*
import com.example.helptataapp.viewmodel.RegisterViewModel

@Composable
fun TelefonoScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    var error by remember { mutableStateOf("") }

    HtFormBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 36.dp)
        ) {
            HtStepHeader(
                currentStep = 6,
                totalSteps  = 7,
                stepTitle   = "¿Cuál es tu teléfono?"
            )

            Text(
                text  = "Ingresa tu número de celular chileno sin el +56.\nEjemplo: si tu número es +56 9 1234 5678, escribe solo 912345678.",
                style = MaterialTheme.typography.bodyMedium,
                color = HT_TextMid
            )

            Spacer(Modifier.height(24.dp))

            HtTextField(
                value         = viewModel.telefono_usuario.value,
                onValueChange = {
                    // Solo permite dígitos y máximo 9 caracteres
                    if (it.all { c -> c.isDigit() } && it.length <= 9) {
                        viewModel.telefono_usuario.value = it
                        if (error.isNotEmpty()) error = ""
                    }
                },
                label         = "Teléfono celular",
                placeholder   = "912345678",
                errorMessage  = error,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                )
            )

            Spacer(Modifier.height(32.dp))

            HtPrimaryButton(
                text    = "Continuar →",
                onClick = {
                    when {
                        viewModel.telefono_usuario.value.isBlank() ->
                            error = "Por favor ingresa tu número de teléfono."
                        viewModel.telefono_usuario.value.length < 9 ->
                            error = "El número debe tener 9 dígitos. Ejemplo: 912345678"
                        else -> navController.navigate("password")
                    }
                }
            )
        }
    }
}
