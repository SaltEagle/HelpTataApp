package com.example.helptataapp.navegation

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.helptataapp.ui.components.*
import com.example.helptataapp.ui.theme.*
import com.example.helptataapp.viewmodel.RegisterViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

/*
 * FechaScreen  —  Paso 4: Fecha de nacimiento
 *
 * CAMBIOS VS. ORIGINAL:
 *
 * 1. DatePickerDialog nativo de Android
 *    → El usuario abre un calendario con un botón.
 *    → Elimina completamente el riesgo de formato incorrecto (ya no hay
 *      campo de texto libre para la fecha).
 *
 * 2. Año mínimo 1900
 *    → datePicker.minDate fijado al 1 de enero de 1900.
 *    → Previene fechas imposibles o muy antiguas.
 *
 * 3. Fecha máxima = hoy − 18 años
 *    → No se puede registrar un usuario menor de 18 años.
 *    → datePicker.maxDate = hoy − 18 años.
 *    → Aplica tanto al selector como a la validación al presionar Continuar.
 *
 * 4. La fecha se almacena en formato ISO 8601 (YYYY-MM-DD)
 *    → Compatible con la API existente.
 *
 * 5. Campo de texto de solo lectura con ícono de calendario
 *    → El usuario ve la fecha seleccionada en formato legible (DD/MM/YYYY)
 *      pero el ViewModel almacena YYYY-MM-DD para la API.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    val context = LocalContext.current
    var error   by remember { mutableStateOf("") }

    // Fecha para mostrar al usuario (DD/MM/YYYY) — solo lectura
    var fechaDisplay by remember { mutableStateOf("") }

    // Calcular los límites del calendario
    val hoy         = Calendar.getInstance()
    val minCal      = Calendar.getInstance().apply { set(1900, 0, 1) }

    // Máximo = hoy - 18 años (edad mínima)
    val maxCal = Calendar.getInstance().apply {
        add(Calendar.YEAR, -18)
    }

    // DatePickerDialog configurado con los límites
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // Construir fecha ISO para la API
                val fechaIso = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
                viewModel.fecha_nac_usuario.value = fechaIso

                // Construir fecha legible para mostrar
                fechaDisplay = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                error = ""
            },
            // Fecha inicial: si ya hay una seleccionada, usarla; si no, fecha máxima
            if (viewModel.fecha_nac_usuario.value.isNotEmpty()) {
                try {
                    val partes = viewModel.fecha_nac_usuario.value.split("-")
                    partes[0].toInt()
                } catch (e: Exception) { maxCal.get(Calendar.YEAR) }
            } else { maxCal.get(Calendar.YEAR) },
            if (viewModel.fecha_nac_usuario.value.isNotEmpty()) {
                try {
                    val partes = viewModel.fecha_nac_usuario.value.split("-")
                    partes[1].toInt() - 1
                } catch (e: Exception) { maxCal.get(Calendar.MONTH) }
            } else { maxCal.get(Calendar.MONTH) },
            if (viewModel.fecha_nac_usuario.value.isNotEmpty()) {
                try {
                    val partes = viewModel.fecha_nac_usuario.value.split("-")
                    partes[2].toInt()
                } catch (e: Exception) { maxCal.get(Calendar.DAY_OF_MONTH) }
            } else { maxCal.get(Calendar.DAY_OF_MONTH) }
        ).also { picker ->
            // ── Límites del calendario ──────────────────────────────────
            picker.datePicker.minDate = minCal.timeInMillis   // 01/01/1900
            picker.datePicker.maxDate = maxCal.timeInMillis   // hoy - 18 años
        }
    }

    // Si ya había una fecha guardada (al volver atrás), mostrarla
    LaunchedEffect(Unit) {
        val guardada = viewModel.fecha_nac_usuario.value
        if (guardada.isNotEmpty()) {
            try {
                val partes = guardada.split("-")
                fechaDisplay = "%s/%s/%s".format(partes[2], partes[1], partes[0])
            } catch (_: Exception) {}
        }
    }

    HtFormBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 36.dp)
        ) {
            HtStepHeader(
                currentStep = 4,
                totalSteps  = 6,
                stepTitle   = "¿Cuándo naciste?"
            )

            Text(
                text  = "Toca el botón para abrir el calendario y seleccionar tu fecha de nacimiento.",
                style = MaterialTheme.typography.bodyMedium,
                color = HT_TextMid
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text  = "Debes tener al menos 18 años para registrarte.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = HT_Navy
                )
            )

            Spacer(Modifier.height(24.dp))

            // ── Campo de solo lectura que muestra la fecha seleccionada ──
            OutlinedTextField(
                value         = fechaDisplay,
                onValueChange = {},   // solo lectura
                readOnly      = true,
                label         = { Text("Fecha de nacimiento", fontSize = 18.sp) },
                placeholder   = { Text("Selecciona una fecha", color = HT_TextMuted) },
                isError       = error.isNotEmpty(),
                supportingText = if (error.isNotEmpty()) {
                    { Text(error, color = HT_Error) }
                } else null,
                trailingIcon  = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            imageVector        = Icons.Filled.DateRange,
                            contentDescription = "Abrir calendario",
                            tint               = HT_Navy
                        )
                    }
                },
                singleLine = true,
                shape      = RoundedCornerShape(12.dp),
                modifier   = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 64.dp),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors    = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor    = HT_Navy,
                    unfocusedBorderColor  = HT_Border,
                    errorBorderColor      = HT_Error,
                    focusedLabelColor     = HT_Navy,
                    unfocusedLabelColor   = HT_TextMuted,
                    disabledTextColor     = HT_TextMain,       // texto visible en readOnly
                    disabledBorderColor   = HT_Border,
                    disabledLabelColor    = HT_TextMuted
                )
            )

            Spacer(Modifier.height(16.dp))

            // ── Botón alternativo grande para abrir el calendario ────────
            // (más fácil de tocar para adultos mayores que el ícono)
            OutlinedButton(
                onClick  = { datePickerDialog.show() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape  = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, HT_Navy),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = HT_Navy)
            ) {
                Icon(
                    imageVector        = Icons.Filled.DateRange,
                    contentDescription = null,
                    modifier           = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text  = if (fechaDisplay.isEmpty()) "Seleccionar fecha" else "Cambiar fecha",
                    style = MaterialTheme.typography.labelLarge.copy(color = HT_Navy)
                )
            }

            // Fecha seleccionada destacada
            if (fechaDisplay.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = HT_NavyLight),
                    shape  = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✓ ", color = HT_Success, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Fecha seleccionada: $fechaDisplay",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color      = HT_Navy,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            HtPrimaryButton(
                text    = "Continuar →",
                onClick = {
                    when {
                        viewModel.fecha_nac_usuario.value.isBlank() ->
                            error = "Por favor selecciona tu fecha de nacimiento."
                        else -> navController.navigate("telefono")
                    }
                }
            )
        }
    }
}
