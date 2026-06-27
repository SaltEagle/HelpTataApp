package com.example.helptataapp.navegation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.helptataapp.ui.components.*
import com.example.helptataapp.ui.theme.*
import com.example.helptataapp.util.*
import com.example.helptataapp.viewmodel.RegisterViewModel

/*
 * RunScreen  —  Paso 1: Ingreso del RUT completo
 *
 * CAMBIOS VS. ORIGINAL:
 *
 * 1. RUT y DV en una SOLA pantalla
 *    → Ya no existe DvRunScreen separado. Se unifican para que el usuario
 *      vea el RUT completo y pueda detectar errores fácilmente.
 *
 * 2. Formato automático mientras escribe
 *    → El campo aplica formatearRut() en cada pulsación:
 *      el usuario escribe "12345678k" y ve "12.345.678-K"
 *    → Solo acepta dígitos y K (filtra cualquier otro carácter)
 *
 * 3. Validación del dígito verificador (módulo 11)
 *    → Al presionar Continuar se llama validarRut() que comprueba
 *      matemáticamente que el DV sea correcto.
 *    → Previene RUTs inventados o con errores de tipeo.
 *
 * 4. Rango numérico
 *    → El cuerpo del RUT debe estar entre 1.000.000 y 99.999.999
 *      (rango válido del Registro Civil chileno).
 *
 * 5. El ViewModel almacena cuerpo y DV por separado
 *    → viewModel.run_usuario = "12345678"
 *    → viewModel.dvrun_usuario = "K"
 *    → Así la API recibe los campos que espera.
 *
 * 6. Feedback visual inmediato
 *    → Muestra ✓ verde o ✗ rojo mientras el usuario escribe
 *      (solo después de que tiene ≥ 8 caracteres formateados).
 */
@Composable
fun RunScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    // TextFieldValue permite controlar texto Y posición del cursor juntos
    var rutDisplay by remember { mutableStateOf(TextFieldValue("")) }
    var error      by remember { mutableStateOf("") }

    // Estado de validación en tiempo real (null = sin validar aún)
    val rutValido: Boolean? = remember(rutDisplay.text) {
        val clean = rutDisplay.text.filter { it.isDigit() || it == 'K' || it == 'k' }
        if (clean.length >= 8) validarRut(rutDisplay.text) else null
    }

    HtFormBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 36.dp)
        ) {
            HtStepHeader(
                currentStep = 1,
                totalSteps  = 6,   // ahora son 6 pasos (RUT unificado)
                stepTitle   = "¿Cuál es tu RUT?"
            )

            Text(
                text  = "Escribe tu RUT completo. El formato se aplicará automáticamente.",
                style = MaterialTheme.typography.bodyMedium,
                color = HT_TextMid
            )

            Spacer(Modifier.height(8.dp))

            // Ejemplo visual del formato
            Text(
                buildAnnotatedString {
                    append("Ejemplo: ")
                    withStyle(SpanStyle(color = HT_Navy, fontWeight = FontWeight.Bold)) {
                        append("12.345.678-9")
                    }
                },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(20.dp))

            // ── Campo de RUT con formato automático ─────────────────────
            OutlinedTextField(
                value         = rutDisplay,
                onValueChange = { tfv ->
                    val filtrado = tfv.text.uppercase().filter { it.isDigit() || it == 'K' }
                    val limitado = filtrado.take(9)
                    val formatted = formatearRut(limitado)
                    // Cursor siempre al final — evita el salto de posición al insertar puntos
                    rutDisplay = TextFieldValue(
                        text      = formatted,
                        selection = TextRange(formatted.length)
                    )
                    if (error.isNotEmpty()) error = ""
                },
                label    = { Text("RUT", fontSize = 18.sp) },
                placeholder = { Text("12.345.678-9", color = HT_TextMuted) },
                isError  = error.isNotEmpty(),
                supportingText = if (error.isNotEmpty()) {
                    { Text(error, color = HT_Error) }
                } else null,
                trailingIcon = {
                    // Indicador visual en tiempo real (solo cuando hay suficientes chars)
                    when (rutValido) {
                        true  -> Text("✓", color = HT_Success, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        false -> Text("✗", color = HT_Error,   fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        null  -> {}
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                singleLine      = true,
                shape           = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                modifier        = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 64.dp),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors    = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = HT_Navy,
                    unfocusedBorderColor = HT_Border,
                    errorBorderColor     = HT_Error,
                    focusedLabelColor    = HT_Navy,
                    unfocusedLabelColor  = HT_TextMuted,
                    cursorColor          = HT_Navy
                )
            )

            // Mensaje de ayuda bajo el campo (cuando el RUT es inválido en tiempo real)
            if (rutValido == false && error.isEmpty() && rutDisplay.text.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text  = "El dígito verificador no coincide con el RUT.",
                    color = HT_Error,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(Modifier.height(32.dp))

            HtPrimaryButton(
                text    = "Continuar →",
                onClick = {
                    when {
                        rutDisplay.text.isBlank() -> {
                            error = "Por favor ingresa tu RUT."
                        }
                        rutValido != true -> {
                            error = "El RUT ingresado no es válido. Revisa el dígito verificador."
                        }
                        else -> {
                            viewModel.run_usuario.value   = rutSinFormato(rutDisplay.text)
                            viewModel.dvrun_usuario.value = dvDeRut(rutDisplay.text)
                            navController.navigate("nombre")
                        }
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            // Nota informativa
            Text(
                text  = "Tu RUT es tu número de identidad chileno (cédula de identidad).",
                style = MaterialTheme.typography.labelMedium,
                color = HT_TextMuted
            )

            Spacer(Modifier.height(24.dp))

            // Link a inicio de sesión
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text  = "¿Ya tienes cuenta? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HT_TextMid
                )
                TextButton(
                    onClick        = {
                        navController.navigate("login") {
                            popUpTo("run") { inclusive = true }
                        }
                    },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text  = "Inicia sesión aquí",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color      = HT_Navy,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
