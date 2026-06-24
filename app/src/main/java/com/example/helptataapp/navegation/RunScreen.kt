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
fun RunScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    var error by remember { mutableStateOf("") }

    HtFormBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 36.dp),
            verticalArrangement = Arrangement.Top
        ) {
            HtStepHeader(
                currentStep = 1,
                totalSteps  = 7,
                stepTitle   = "¿Cuál es tu RUN?"
            )

            Text(
                text  = "El RUN es tu número de identificación chileno. Solo los números, sin el dígito verificador.",
                style = MaterialTheme.typography.bodyMedium,
                color = HT_TextMid
            )

            Spacer(Modifier.height(24.dp))

            HtTextField(
                value         = viewModel.run_usuario.value,
                onValueChange = {
                    viewModel.run_usuario.value = it
                    if (error.isNotEmpty()) error = ""
                },
                label         = "RUN (sin dígito verificador)",
                placeholder   = "Ej: 12345678",
                errorMessage  = error,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(Modifier.height(32.dp))

            HtPrimaryButton(
                text    = "Continuar →",
                onClick = {
                    if (viewModel.run_usuario.value.isBlank()) {
                        error = "Por favor ingresa tu RUN."
                    } else {
                        navController.navigate("dvrun")
                    }
                }
            )
        }
    }
}
