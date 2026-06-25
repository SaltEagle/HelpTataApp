package com.example.helptataapp.navegation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.helptataapp.ui.components.*
import com.example.helptataapp.ui.theme.*
import com.example.helptataapp.viewmodel.RegisterViewModel

// ── Dominios desechables bloqueados ─────────────────────────────────────────
// Lista de servicios de correo temporal más comunes.
// Ampliar según sea necesario.
private val DOMINIOS_BLOQUEADOS = setOf(
    "mailinator.com", "tempmail.com", "guerrillamail.com", "10minutemail.com",
    "throwaway.email", "yopmail.com", "sharklasers.com", "guerrillamailblock.com",
    "fakeinbox.com", "trashmail.com", "maildrop.cc", "dispostable.com",
    "spamgourmet.com", "tempr.email", "discard.email", "mailnull.com"
)

/**
 * Valida un correo electrónico con reglas de seguridad básicas.
 *
 * Reglas aplicadas:
 *  1. No puede estar vacío.
 *  2. Debe tener formato válido (regex RFC 5322 simplificado).
 *  3. La parte local (antes del @) debe tener al menos 3 caracteres.
 *  4. El dominio debe tener al menos un punto después del @.
 *  5. No puede terminar en puntos consecutivos (..).
 *  6. No puede ser de un dominio de correo temporal/desechable.
 *  7. Máximo 254 caracteres (límite RFC 5321).
 *
 * Retorna null si es válido, o un String con el mensaje de error.
 */
fun validarCorreo(correo: String): String? {
    val c = correo.trim()

    if (c.isBlank())
        return "El correo electrónico es obligatorio."

    if (c.length > 254)
        return "El correo no puede tener más de 254 caracteres."

    if (c.contains(".."))
        return "El correo no puede tener puntos consecutivos."

    // Regex: localpart@dominio.tld
    val emailRegex = Regex(
        "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
    )
    if (!emailRegex.matches(c))
        return "Ingresa un correo electrónico válido. Ejemplo: nombre@gmail.com"

    // Parte local mínimo 3 caracteres
    val localPart = c.substringBefore("@")
    if (localPart.length < 3)
        return "La parte antes del @ debe tener al menos 3 caracteres."

    // Dominio no desechable
    val dominio = c.substringAfter("@").lowercase()
    if (DOMINIOS_BLOQUEADOS.any { dominio == it || dominio.endsWith(".$it") })
        return "Este tipo de correo no está permitido. Usa tu correo personal."

    return null  // válido
}

/*
 * CorreoScreen  —  Paso 6 de 7: Correo electrónico
 *
 * Posición en el flujo:
 *   telefono → correo → password
 *
 * Funcionalidades:
 *
 * 1. Campo de correo con teclado de email
 *    → KeyboardType.Email abre el teclado con @ visible en Android.
 *    → autoCorrect = false y capitalization = None evitan que Android
 *      modifique el texto del correo.
 *
 * 2. Campo de confirmación de correo
 *    → Previene errores de tipeo (muy comunes con correos largos).
 *    → Error claro si no coinciden.
 *
 * 3. Validación completa con validarCorreo()
 *    → Formato, longitud, puntos consecutivos, dominios temporales.
 *
 * 4. Normalización automática
 *    → El correo se convierte a minúsculas antes de guardarse.
 *    → Se eliminan espacios al inicio y al final (trim).
 *
 * 5. Indicador visual en tiempo real
 *    → ✓ verde cuando el correo tiene formato válido (sin esperar Continuar).
 *    → Solo aparece después de que el usuario escribe un @.
 *
 * 6. El ViewModel almacena el correo en viewModel.correo_usuario.value
 *    → Asegúrate de agregar este campo al RegisterViewModel
 *      (ver nota al final del archivo).
 */
@Composable
fun CorreoScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    var errorCorreo   by remember { mutableStateOf("") }
    var errorConfirm  by remember { mutableStateOf("") }
    var confirmCorreo by remember { mutableStateOf("") }

    // Validación en tiempo real (solo si ya escribió el @)
    val correoValido: Boolean? = remember(viewModel.correo_usuario.value) {
        val c = viewModel.correo_usuario.value
        if (c.contains("@") && c.length > 5) validarCorreo(c) == null else null
    }

    HtFormBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 36.dp)
        ) {

            // ── Encabezado de paso ───────────────────────────────────────
            HtStepHeader(
                currentStep = 6,
                totalSteps  = 7,
                stepTitle   = "¿Cuál es tu correo?"
            )

            Text(
                text  = "Tu correo electrónico se usará para iniciar sesión y recuperar tu cuenta.",
                style = MaterialTheme.typography.bodyMedium,
                color = HT_TextMid
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text  = "Ejemplo: nombre@gmail.com",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color      = HT_Navy,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(24.dp))

            // ── Campo correo electrónico ─────────────────────────────────
            OutlinedTextField(
                value         = viewModel.correo_usuario.value,
                onValueChange = { input ->
                    // Normalizar: minúsculas, sin espacios
                    val normalizado = input.lowercase().trim()
                    viewModel.correo_usuario.value = normalizado
                    if (errorCorreo.isNotEmpty()) errorCorreo = ""
                    // Si ya confirmó y cambia el principal, limpiar confirmación
                    if (confirmCorreo.isNotEmpty() &&
                        normalizado != confirmCorreo
                    ) errorConfirm = ""
                },
                label    = { Text("Correo electrónico", fontSize = 18.sp) },
                placeholder = {
                    Text("nombre@gmail.com", color = HT_TextMuted)
                },
                isError = errorCorreo.isNotEmpty(),
                supportingText = if (errorCorreo.isNotEmpty()) {
                    { Text(errorCorreo, color = HT_Error) }
                } else null,
                leadingIcon = {
                    Icon(
                        imageVector        = Icons.Filled.Email,
                        contentDescription = null,
                        tint               = HT_Navy
                    )
                },
                trailingIcon = {
                    when (correoValido) {
                        true  -> Text(
                            "✓",
                            color      = HT_Success,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 20.sp
                        )
                        false -> Text(
                            "✗",
                            color      = HT_Error,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 20.sp
                        )
                        null  -> {}
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType   = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect    = false
                ),
                singleLine = true,
                shape      = RoundedCornerShape(12.dp),
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

            Spacer(Modifier.height(16.dp))

            // ── Campo confirmar correo ───────────────────────────────────
            OutlinedTextField(
                value         = confirmCorreo,
                onValueChange = { input ->
                    confirmCorreo = input.lowercase().trim()
                    if (errorConfirm.isNotEmpty()) errorConfirm = ""
                },
                label    = { Text("Repite el correo", fontSize = 18.sp) },
                placeholder = {
                    Text("nombre@gmail.com", color = HT_TextMuted)
                },
                isError = errorConfirm.isNotEmpty(),
                supportingText = if (errorConfirm.isNotEmpty()) {
                    { Text(errorConfirm, color = HT_Error) }
                } else null,
                leadingIcon = {
                    Icon(
                        imageVector        = Icons.Filled.Email,
                        contentDescription = null,
                        tint               = if (errorConfirm.isEmpty()) HT_TextMuted
                                            else HT_Error
                    )
                },
                // ✓ verde si coincide con el correo principal
                trailingIcon = if (
                    confirmCorreo.isNotEmpty() &&
                    confirmCorreo == viewModel.correo_usuario.value
                ) {
                    {
                        Text(
                            "✓",
                            color      = HT_Success,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 20.sp
                        )
                    }
                } else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType   = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect    = false
                ),
                singleLine = true,
                shape      = RoundedCornerShape(12.dp),
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

            Spacer(Modifier.height(12.dp))

            // ── Reglas visibles ──────────────────────────────────────────
            val correo = viewModel.correo_usuario.value
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                ReglaCorreo(
                    texto    = "Formato válido (nombre@dominio.com)",
                    cumplida = correo.contains("@") &&
                               validarCorreo(correo) == null
                )
                ReglaCorreo(
                    texto    = "Los correos coinciden",
                    cumplida = confirmCorreo.isNotEmpty() &&
                               confirmCorreo == correo
                )
                ReglaCorreo(
                    texto    = "Correo personal (no desechable)",
                    cumplida = correo.contains("@") &&
                               DOMINIOS_BLOQUEADOS.none {
                                   correo.substringAfter("@").lowercase() == it
                               }
                )
            }

            Spacer(Modifier.height(32.dp))

            // ── Botón continuar ──────────────────────────────────────────
            HtPrimaryButton(
                text    = "Continuar →",
                onClick = {
                    var ok = true

                    // Validar correo principal
                    val errCorreo = validarCorreo(viewModel.correo_usuario.value)
                    if (errCorreo != null) {
                        errorCorreo = errCorreo
                        ok = false
                    }

                    // Validar confirmación
                    if (confirmCorreo.isBlank()) {
                        errorConfirm = "Por favor repite tu correo."
                        ok = false
                    } else if (confirmCorreo != viewModel.correo_usuario.value) {
                        errorConfirm = "Los correos no coinciden. Revísalos."
                        ok = false
                    }

                    if (ok) {
                        navController.navigate("password")
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            // ── Nota de privacidad ───────────────────────────────────────
            Card(
                colors = CardDefaults.cardColors(containerColor = HT_NavyLight),
                shape  = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector        = Icons.Filled.Email,
                        contentDescription = null,
                        tint               = HT_Navy,
                        modifier           = Modifier.size(22.dp)
                    )
                    Text(
                        text  = "Tu correo no será compartido con terceros y solo se usará para acceder a tu cuenta.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color    = HT_Navy,
                            fontSize = 15.sp
                        )
                    )
                }
            }
        }
    }
}

// ── Componente de regla individual ───────────────────────────────────────────
@Composable
private fun ReglaCorreo(texto: String, cumplida: Boolean) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text       = if (cumplida) "✓" else "○",
            color      = if (cumplida) HT_Success else HT_TextMuted,
            fontWeight = FontWeight.Bold,
            fontSize   = 16.sp
        )
        Text(
            text  = texto,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (cumplida) HT_Success else HT_TextMuted
            )
        )
    }
}

/*
 * ════════════════════════════════════════════════════════════════════════════
 *  NOTA PARA EL VIEWMODEL
 *  Agrega este campo en RegisterViewModel.kt:
 *
 *      var correo_usuario = mutableStateOf("")
 *
 *  Y en UsuarioRequest (si la API lo requiere):
 *
 *      val correo_usuario: String
 * ════════════════════════════════════════════════════════════════════════════
 */
