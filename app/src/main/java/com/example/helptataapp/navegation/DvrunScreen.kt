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
fun DvRunScreen(
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
                currentStep = 2,
                totalSteps  = 7,
                stepTitle   = "Dígito verificador"
            )

            Text(
                text  = "Es el número o letra que aparece después del guión en tu RUN. Ejemplo: si tu RUN es 12345678-K, el dígito es K.",
                style = MaterialTheme.typography.bodyMedium,
                color = HT_TextMid
            )

            Spacer(Modifier.height(24.dp))

            HtTextField(
                value         = viewModel.dvrun_usuario.value,
                onValueChange = {
                    if (it.length <= 1) {
                        viewModel.dvrun_usuario.value = it.uppercase()
                        if (error.isNotEmpty()) error = ""
                    }
                },
                label        = "Dígito verificador",
                placeholder  = "Ej: K o 9",
                errorMessage = error
            )

            Spacer(Modifier.height(32.dp))

            HtPrimaryButton(
                text    = "Continuar →",
                onClick = {
                    if (viewModel.dvrun_usuario.value.isBlank()) {
                        error = "Por favor ingresa el dígito verificador."
                    } else {
                        navController.navigate("nombre")
                    }
                }
            )
        }
    }
}
