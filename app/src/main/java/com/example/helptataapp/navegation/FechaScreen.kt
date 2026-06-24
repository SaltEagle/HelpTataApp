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
fun FechaScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    var error by remember { mutableStateOf("") }

    // Validación de formato YYYY-MM-DD
    fun validarFecha(fecha: String): Boolean {
        val regex = Regex("""^\d{4}-\d{2}-\d{2}$""")
        return regex.matches(fecha)
    }

    HtFormBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 36.dp)
        ) {
            HtStepHeader(
                currentStep = 5,
                totalSteps  = 7,
                stepTitle   = "¿Cuándo naciste?"
            )

            Text(
                text  = "Ingresa tu fecha de nacimiento en el formato indicado:",
                style = MaterialTheme.typography.bodyMedium,
                color = HT_TextMid
            )

            Spacer(Modifier.height(8.dp))

            // Ejemplo visual del formato
            Text(
                text  = "Año - Mes - Día\nEjemplo: 1955-03-20",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = HT_Navy,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            )

            Spacer(Modifier.height(20.dp))

            HtTextField(
                value         = viewModel.fecha_nac_usuario.value,
                onValueChange = {
                    viewModel.fecha_nac_usuario.value = it
                    if (error.isNotEmpty()) error = ""
                },
                label         = "Fecha de nacimiento",
                placeholder   = "1955-03-20",
                errorMessage  = error,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(Modifier.height(32.dp))

            HtPrimaryButton(
                text    = "Continuar →",
                onClick = {
                    when {
                        viewModel.fecha_nac_usuario.value.isBlank() ->
                            error = "Por favor ingresa tu fecha de nacimiento."
                        !validarFecha(viewModel.fecha_nac_usuario.value) ->
                            error = "El formato debe ser AAAA-MM-DD. Ejemplo: 1955-03-20"
                        else -> navController.navigate("telefono")
                    }
                }
            )
        }
    }
}
