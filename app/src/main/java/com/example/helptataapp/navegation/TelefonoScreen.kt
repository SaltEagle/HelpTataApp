package com.example.helptataapp.navegation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.helptataapp.ui.components.*
import com.example.helptataapp.ui.theme.*
import com.example.helptataapp.util.*
import com.example.helptataapp.viewmodel.RegisterViewModel

/*
 * TelefonoScreen  —  Paso 5: Teléfono celular
 *
 * CAMBIOS VS. ORIGINAL:
 *
 * 1. Solo acepta dígitos (filtrarTelefono)
 *    → Cualquier letra, símbolo o espacio se descarta silenciosamente.
 *
 * 2. Debe empezar con 9
 *    → Celulares chilenos siempre empiezan en 9.
 *    → Si el primer dígito no es 9, se muestra error inmediato.
 *
 * 3. Exactamente 9 dígitos
 *    → El campo se bloquea al llegar a 9 (take(9) en filtrarTelefono).
 *    → No se puede enviar con menos de 9.
 *
 * 4. Prefijo +56 visible pero no editable
 *    → El usuario ve que es un número chileno.
 *    → El prefijo NO se guarda en el ViewModel (la API recibe solo el número).
 *
 * 5. Contador de dígitos en tiempo real
 *    → Muestra "X / 9" para orientar al usuario.
 */
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
                currentStep = 5,
                totalSteps  = 6,
                stepTitle   = "¿Cuál es tu celular?"
            )

            Text(
                text  = "Ingresa tu número de celular chileno sin el código de país.",
                style = MaterialTheme.typography.bodyMedium,
                color = HT_TextMid
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text  = "Ejemplo: si tu número es +56 9 1234 5678, escribe 912345678",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color      = HT_Navy,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(20.dp))

            // ── Campo con prefijo +56 ────────────────────────────────────
            OutlinedTextField(
                value         = viewModel.telefono_usuario.value,
                onValueChange = { input ->
                    val filtrado = filtrarTelefono(input)
                    viewModel.telefono_usuario.value = filtrado
                    if (error.isNotEmpty()) error = ""
                },
                label    = { Text("Número de celular", fontSize = 18.sp) },
                placeholder = { Text("912345678", color = HT_TextMuted) },
                isError  = error.isNotEmpty(),
                supportingText = if (error.isNotEmpty()) {
                    { Text(error, color = HT_Error) }
                } else null,
                // Prefijo +56 fijo a la izquierda
                leadingIcon = {
                    Text(
                        " +56 ",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color      = HT_Navy,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                // Contador de dígitos a la derecha
                trailingIcon = {
                    val count = viewModel.telefono_usuario.value.length
                    Text(
                        "$count/9",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (count == 9) HT_Success else HT_TextMuted
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine      = true,
                shape           = RoundedCornerShape(12.dp),
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

            Spacer(Modifier.height(32.dp))

            HtPrimaryButton(
                text    = "Continuar →",
                onClick = {
                    val err = validarTelefono(viewModel.telefono_usuario.value)
                    if (err != null) {
                        error = err
                    } else {
                        navController.navigate("correo")  // antes decía "password"
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text  = "Tu número no será visible para otros usuarios.",
                style = MaterialTheme.typography.labelMedium,
                color = HT_TextMuted
            )
        }
    }
}
