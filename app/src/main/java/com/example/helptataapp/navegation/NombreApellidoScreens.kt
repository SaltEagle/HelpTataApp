package com.example.helptataapp.navegation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.helptataapp.ui.components.*
import com.example.helptataapp.ui.theme.*
import com.example.helptataapp.util.*
import com.example.helptataapp.viewmodel.RegisterViewModel

/*
 * NombreScreen  —  Paso 2: Nombre del usuario
 *
 * CAMBIOS VS. ORIGINAL:
 *
 * 1. Solo letras y espacios
 *    → filtrarNombre() elimina silenciosamente dígitos, símbolos y emojis.
 *    → Previene inyección de datos y nombres claramente inválidos.
 *
 * 2. Máximo 50 caracteres por campo
 *    → Contador visual "X / 50" para que el usuario sepa cuánto le queda.
 *
 * 3. Capitalización automática
 *    → KeyboardCapitalization.Words → el teclado Android capitaliza cada
 *      palabra automáticamente (Juan Carlos en vez de juan carlos).
 *
 * 4. Segundo nombre opcional claramente indicado
 *    → El label dice "(opcional)" para no confundir.
 *
 * 5. Validación mínima de 2 caracteres para el primer nombre
 */
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
                currentStep = 2,
                totalSteps  = 6,
                stepTitle   = "¿Cuál es tu nombre?"
            )

            // ── Primer nombre (obligatorio) ──────────────────────────────
            NombreTextField(
                value        = viewModel.pnombre_usuario.value,
                onValueChange = {
                    viewModel.pnombre_usuario.value = filtrarNombre(it)
                    if (errorPnombre.isNotEmpty()) errorPnombre = ""
                },
                label        = "Primer nombre *",
                placeholder  = "Ej: Juan",
                errorMessage = errorPnombre
            )

            Spacer(Modifier.height(16.dp))

            // ── Segundo nombre (opcional) ────────────────────────────────
            NombreTextField(
                value        = viewModel.snombre_usuario.value ?: "",
                onValueChange = {
                    val filtrado = filtrarNombre(it)
                    viewModel.snombre_usuario.value = filtrado.ifBlank { "" }
                },
                label       = "Segundo nombre (opcional)",
                placeholder = "Ej: Carlos"
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text  = "* Campo obligatorio  •  Solo se permiten letras",
                style = MaterialTheme.typography.labelMedium,
                color = HT_TextMuted
            )

            Spacer(Modifier.height(32.dp))

            HtPrimaryButton(
                text    = "Continuar →",
                onClick = {
                    val err = validarNombre(viewModel.pnombre_usuario.value)
                    if (err != null) {
                        errorPnombre = err
                    } else {
                        navController.navigate("apellido")
                    }
                }
            )
        }
    }
}

/*
 * ApellidoScreen  —  Paso 3: Apellido del usuario
 *
 * Mismas reglas que NombreScreen:
 *  • Solo letras y espacios
 *  • Máximo 50 caracteres
 *  • Capitalización automática
 *  • Segundo apellido opcional
 */
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
                currentStep = 3,
                totalSteps  = 6,
                stepTitle   = "¿Cuál es tu apellido?"
            )

            // ── Primer apellido (obligatorio) ────────────────────────────
            NombreTextField(
                value        = viewModel.papellido_usuario.value,
                onValueChange = {
                    viewModel.papellido_usuario.value = filtrarNombre(it)
                    if (errorPapellido.isNotEmpty()) errorPapellido = ""
                },
                label        = "Primer apellido *",
                placeholder  = "Ej: González",
                errorMessage = errorPapellido
            )

            Spacer(Modifier.height(16.dp))

            // ── Segundo apellido (opcional) ──────────────────────────────
            NombreTextField(
                value        = viewModel.sapellido_usuario.value ?: "",
                onValueChange = {
                    val filtrado = filtrarNombre(it)
                    viewModel.sapellido_usuario.value = filtrado.ifBlank { "" }
                },
                label       = "Segundo apellido (opcional)",
                placeholder = "Ej: Martínez"
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text  = "* Campo obligatorio  •  Solo se permiten letras",
                style = MaterialTheme.typography.labelMedium,
                color = HT_TextMuted
            )

            Spacer(Modifier.height(32.dp))

            HtPrimaryButton(
                text    = "Continuar →",
                onClick = {
                    val err = validarNombre(viewModel.papellido_usuario.value)
                    if (err != null) {
                        errorPapellido = err
                    } else {
                        navController.navigate("fecha")
                    }
                }
            )
        }
    }
}

// ── Componente interno: campo de nombre con contador ─────────────────────────
@Composable
private fun NombreTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    errorMessage: String = ""
) {
    val MAX = 50
    Column {
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            label         = { Text(label, fontSize = 18.sp) },
            placeholder   = { Text(placeholder, color = HT_TextMuted) },
            isError       = errorMessage.isNotEmpty(),
            supportingText = if (errorMessage.isNotEmpty()) {
                { Text(errorMessage, color = HT_Error) }
            } else null,
            // Contador de caracteres en la esquina derecha
            trailingIcon = {
                Text(
                    "${value.length}/$MAX",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (value.length >= MAX) HT_Error else HT_TextMuted
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType    = KeyboardType.Text,
                capitalization  = KeyboardCapitalization.Words,
                autoCorrect     = false   // evita que Android "corrija" nombres propios
            ),
            singleLine = true,
            shape      = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            modifier   = Modifier
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
    }
}
