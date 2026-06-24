package com.example.helptataapp.navegation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.helptataapp.ui.components.*
import com.example.helptataapp.ui.theme.*
import com.example.helptataapp.viewmodel.RegisterViewModel

@Composable
fun ApellidoScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    var errorPapellido by remember { mutableStateOf("") }

    HtFormBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 36.dp)
        ) {
            HtStepHeader(
                currentStep = 4,
                totalSteps  = 7,
                stepTitle   = "¿Cuál es tu apellido?"
            )

            // Primer apellido (obligatorio)
            HtTextField(
                value         = viewModel.papellido_usuario.value,
                onValueChange = {
                    viewModel.papellido_usuario.value = it
                    if (errorPapellido.isNotEmpty()) errorPapellido = ""
                },
                label        = "Primer apellido *",
                placeholder  = "Ej: González",
                errorMessage = errorPapellido
            )

            Spacer(Modifier.height(16.dp))

            // Segundo apellido (opcional)
            HtTextField(
                value         = viewModel.sapellido_usuario.value ?: "",
                onValueChange = { viewModel.sapellido_usuario.value = it.ifBlank { "" } },
                label         = "Segundo apellido (opcional)",
                placeholder   = "Ej: Martínez"
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text  = "* Campo obligatorio",
                style = MaterialTheme.typography.labelMedium,
                color = HT_TextMuted
            )

            Spacer(Modifier.height(32.dp))

            HtPrimaryButton(
                text    = "Continuar →",
                onClick = {
                    if (viewModel.papellido_usuario.value.isBlank()) {
                        errorPapellido = "Por favor ingresa tu primer apellido."
                    } else {
                        navController.navigate("fecha")
                    }
                }
            )
        }
    }
}
