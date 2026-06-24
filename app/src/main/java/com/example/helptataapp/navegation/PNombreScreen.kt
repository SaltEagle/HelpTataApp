package com.example.helptataapp.navegation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.helptataapp.ui.components.*
import com.example.helptataapp.ui.theme.*
import com.example.helptataapp.viewmodel.RegisterViewModel

@Composable
fun NombreScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    var errorPnombre by remember { mutableStateOf("") }

    HtFormBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 36.dp)
        ) {
            HtStepHeader(
                currentStep = 3,
                totalSteps  = 7,
                stepTitle   = "¿Cuál es tu nombre?"
            )

            // Primer nombre (obligatorio)
            HtTextField(
                value         = viewModel.pnombre_usuario.value,
                onValueChange = {
                    viewModel.pnombre_usuario.value = it
                    if (errorPnombre.isNotEmpty()) errorPnombre = ""
                },
                label        = "Primer nombre *",
                placeholder  = "Ej: Juan",
                errorMessage = errorPnombre
            )

            Spacer(Modifier.height(16.dp))

            // Segundo nombre (opcional)
            HtTextField(
                value         = viewModel.snombre_usuario.value ?: "",
                    onValueChange = { viewModel.snombre_usuario.value = it.ifBlank { "" } },
                label         = "Segundo nombre (opcional)",
                placeholder   = "Ej: Carlos"
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
                    if (viewModel.pnombre_usuario.value.isBlank()) {
                        errorPnombre = "Por favor ingresa tu primer nombre."
                    } else {
                        navController.navigate("apellido")
                    }
                }
            )
        }
    }
}
