package com.example.helptataapp.navegation

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.helptataapp.model.UsuarioRequest
import com.example.helptataapp.ui.components.*
import com.example.helptataapp.ui.theme.*
import com.example.helptataapp.util.*
import com.example.helptataapp.viewmodel.RegisterViewModel

/*
 * PasswordScreen  —  Paso 6 (último): Creación de contraseña
 *
 * CAMBIOS VS. ORIGINAL:
 *
 * 1. Campo de confirmación de contraseña
 *    → Previene errores de tipeo al registrarse.
 *    → Error claro si no coinciden.
 *
 * 2. Medidor de fortaleza visual
 *    → Barra animada de 4 segmentos: Muy débil / Débil / Aceptable / Fuerte
 *    → Criterios: longitud ≥ 8, mayúsculas, dígitos, símbolos
 *    → Colores: rojo → naranja → ámbar → verde
 *
 * 3. Botón ojo para mostrar/ocultar contraseña
 *    → Reduce frustración al escribir contraseñas largas.
 *    → Aplica independientemente a cada campo.
 *
 * 4. No permite espacios en la contraseña
 *    → Los espacios son una fuente común de errores invisibles.
 *
 * 5. Mínimo 6 caracteres (validarContrasena)
 *
 * 6. Sin cambios en la lógica de API
 *    → Se mantiene exactamente el mismo UsuarioRequest y llamada a
 *      viewModel.registrarUsuario() del original.
 */
@Composable
fun PasswordScreen(
    viewModel: RegisterViewModel
) {
    val context = LocalContext.current

    var errorPass    by remember { mutableStateOf("") }
    var errorConfirm by remember { mutableStateOf("") }
    var confirmPass  by remember { mutableStateOf("") }
    var showPass     by remember { mutableStateOf(false) }
    var showConfirm  by remember { mutableStateOf(false) }

    // Fortaleza en tiempo real
    val fortaleza = remember(viewModel.password_usuario.value) {
        if (viewModel.password_usuario.value.isEmpty()) null
        else evaluarContrasena(viewModel.password_usuario.value)
    }

    HtFormBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 36.dp)
        ) {
            HtStepHeader(
                currentStep = 6,
                totalSteps  = 6,
                stepTitle   = "Crea tu contraseña"
            )

            Text(
                text  = "Elige una contraseña que recuerdes. Puedes usar letras, números y símbolos.",
                style = MaterialTheme.typography.bodyMedium,
                color = HT_TextMid
            )

            Spacer(Modifier.height(24.dp))

            // ── Campo contraseña ─────────────────────────────────────────
            PasswordField(
                value         = viewModel.password_usuario.value,
                onValueChange = {
                    // No permitir espacios
                    if (!it.contains(' ')) {
                        viewModel.password_usuario.value = it
                        if (errorPass.isNotEmpty()) errorPass = ""
                    }
                },
                label        = "Contraseña",
                errorMessage = errorPass,
                showPassword = showPass,
                onToggleShow = { showPass = !showPass }
            )

            // ── Medidor de fortaleza ─────────────────────────────────────
            if (fortaleza != null) {
                Spacer(Modifier.height(10.dp))
                PasswordStrengthMeter(fortaleza)
            }

            Spacer(Modifier.height(16.dp))

            // ── Campo confirmar contraseña ───────────────────────────────
            PasswordField(
                value         = confirmPass,
                onValueChange = {
                    if (!it.contains(' ')) {
                        confirmPass = it
                        if (errorConfirm.isNotEmpty()) errorConfirm = ""
                    }
                },
                label        = "Repite la contraseña",
                errorMessage = errorConfirm,
                showPassword = showConfirm,
                onToggleShow = { showConfirm = !showConfirm }
            )

            Spacer(Modifier.height(12.dp))

            // ── Reglas visibles ──────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                ReglaClave("Al menos 6 caracteres",       viewModel.password_usuario.value.length >= 6)
                ReglaClave("Sin espacios",                !viewModel.password_usuario.value.contains(' '))
                ReglaClave("Las contraseñas coinciden",   confirmPass == viewModel.password_usuario.value && confirmPass.isNotEmpty())
            }

            Spacer(Modifier.height(32.dp))

            HtPrimaryButton(
                text    = "Finalizar registro",
                onClick = {
                    var ok = true

                    val errPass = validarContrasena(viewModel.password_usuario.value)
                    if (errPass != null) { errorPass = errPass; ok = false }

                    if (confirmPass.isBlank()) {
                        errorConfirm = "Por favor repite tu contraseña."; ok = false
                    } else if (confirmPass != viewModel.password_usuario.value) {
                        errorConfirm = "Las contraseñas no coinciden."; ok = false
                    }

                    if (ok) {
                        val usuario = UsuarioRequest(
                            run_usuario       = viewModel.run_usuario.value,
                            dvrun_usuario     = viewModel.dvrun_usuario.value,
                            pnombre_usuario   = viewModel.pnombre_usuario.value,
                            snombre_usuario   = viewModel.snombre_usuario.value,
                            papellido_usuario = viewModel.papellido_usuario.value,
                            sapellido_usuario = viewModel.sapellido_usuario.value,
                            fecha_nac_usuario = viewModel.fecha_nac_usuario.value,
                            telefono_usuario  = viewModel.telefono_usuario.value.toInt(),
                            correo_usuario = viewModel.correo_usuario.value,
                            password_usuario  = viewModel.password_usuario.value

                        )
                        viewModel.registrarUsuario(usuario)
                        Toast.makeText(context, "Registrando usuario…", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }
}

// ── Campo de contraseña con ojo ──────────────────────────────────────────────
@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String,
    showPassword: Boolean,
    onToggleShow: () -> Unit
) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        label         = { Text(label, fontSize = 18.sp) },
        isError       = errorMessage.isNotEmpty(),
        supportingText = if (errorMessage.isNotEmpty()) {
            { Text(errorMessage, color = HT_Error) }
        } else null,
        visualTransformation = if (showPassword)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggleShow) {
                Icon(
                    imageVector        = if (showPassword) Icons.Filled.VisibilityOff
                                        else Icons.Filled.Visibility,
                    contentDescription = if (showPassword) "Ocultar contraseña"
                                        else "Mostrar contraseña",
                    tint               = HT_Navy
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
}

// ── Medidor de fortaleza de contraseña ──────────────────────────────────────
@Composable
private fun PasswordStrengthMeter(fortaleza: PasswordStrength) {
    val nivel = fortaleza.ordinal + 1   // 1 a 4

    // Color animado de la barra
    val barColor by animateColorAsState(
        targetValue = Color(fortaleza.color),
        label       = "strengthColor"
    )

    Column {
        // Barra segmentada de 4 bloques
        Row(
            modifier            = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(4) { index ->
                val filled = index < nivel
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .let {
                            it.then(
                                Modifier.padding(0.dp)
                            )
                        }
                ) {
                    LinearProgressIndicator(
                        progress   = { if (filled) 1f else 0f },
                        modifier   = Modifier.fillMaxSize(),
                        color      = if (filled) barColor else HT_Border,
                        trackColor = HT_Border
                    )
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text  = "Fortaleza: ${fortaleza.label}",
            style = MaterialTheme.typography.labelMedium.copy(
                color      = Color(fortaleza.color),
                fontWeight = FontWeight.Bold
            )
        )
    }
}

// ── Regla individual de contraseña ───────────────────────────────────────────
@Composable
private fun ReglaClave(texto: String, cumplida: Boolean) {
    Row(
        modifier            = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text  = if (cumplida) "✓" else "○",
            color = if (cumplida) HT_Success else HT_TextMuted,
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
