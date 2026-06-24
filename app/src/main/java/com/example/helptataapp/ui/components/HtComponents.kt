package com.example.helptataapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helptataapp.ui.theme.*

// ═══════════════════════════════════════════════════════════════════════════
//  HtScreenBackground
//  Fondo con gradiente azul marino para WelcomeScreen / LoginScreen
// ═══════════════════════════════════════════════════════════════════════════
@Composable
fun HtGradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(HT_Navy, HT_NavyDark)
                )
            ),
        content = content
    )
}

// ═══════════════════════════════════════════════════════════════════════════
//  HtFormBackground
//  Fondo crema para las pantallas de formulario (registro)
// ═══════════════════════════════════════════════════════════════════════════
@Composable
fun HtFormBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HT_Cream),
        content = content
    )
}

// ═══════════════════════════════════════════════════════════════════════════
//  HtPrimaryButton
//  Botón principal: fondo azul marino, texto blanco, altura ≥ 56dp
//  (área táctil mínima recomendada para adultos mayores — WCAG 2.5.5)
// ═══════════════════════════════════════════════════════════════════════════
@Composable
fun HtPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick  = onClick,
        enabled  = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),           // área táctil generosa
        shape  = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor         = HT_Navy,
            contentColor           = HT_White,
            disabledContainerColor = HT_Border,
            disabledContentColor   = HT_TextMuted
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp
        )
    ) {
        Text(
            text  = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════
//  HtAmberButton
//  Botón de acento ámbar — CTA secundario (Registrarse en Welcome)
// ═══════════════════════════════════════════════════════════════════════════
@Composable
fun HtAmberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick  = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape  = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = HT_Amber,
            contentColor   = HT_TextMain
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp
        )
    ) {
        Text(
            text  = text,
            style = MaterialTheme.typography.labelLarge.copy(
                color = HT_TextMain
            )
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════
//  HtOutlineButton
//  Botón de borde blanco — para usar sobre fondos oscuros (Welcome)
// ═══════════════════════════════════════════════════════════════════════════
@Composable
fun HtOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick  = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape  = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = HT_White
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = HT_White.copy(alpha = 0.7f)
        )
    ) {
        Text(
            text  = text,
            style = MaterialTheme.typography.labelLarge.copy(
                color = HT_White
            )
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════
//  HtTextField
//  Campo de texto accesible con label grande, error inline y soporte
//  para teclado personalizado y transformación visual (contraseña).
//
//  Cambios vs. OutlinedTextField estándar:
//  • minHeight de 64dp → área táctil mayor para adultos mayores
//  • label con fontSize 18sp para que sea legible sin acercarse
//  • supportingText muestra el error inline (WCAG 3.3.1)
//  • isError cambia el borde a rojo + icono de alerta automático de M3
// ═══════════════════════════════════════════════════════════════════════════
@Composable
fun HtTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    errorMessage: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true
) {
    val hasError = errorMessage.isNotEmpty()

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value               = value,
            onValueChange       = onValueChange,
            label               = {
                Text(
                    text  = label,
                    fontSize = 18.sp   // etiqueta grande
                )
            },
            placeholder         = if (placeholder.isNotEmpty()) {
                { Text(placeholder, color = HT_TextMuted) }
            } else null,
            isError             = hasError,
            supportingText      = if (hasError) {
                {
                    Text(
                        text  = errorMessage,
                        color = HT_Error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else null,
            keyboardOptions     = keyboardOptions,
            visualTransformation = visualTransformation,
            singleLine          = singleLine,
            shape               = RoundedCornerShape(12.dp),
            modifier            = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 64.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = OutlinedTextFieldDefaults.colors(
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

// ═══════════════════════════════════════════════════════════════════════════
//  HtStepHeader
//  Encabezado de progreso para las pantallas de registro multi-paso.
//  Muestra el número de paso actual y una barra de progreso visual.
//  Orienta al usuario sobre dónde está (Nielsen #1 Visibilidad del estado).
// ═══════════════════════════════════════════════════════════════════════════
@Composable
fun HtStepHeader(
    currentStep: Int,    // 1-based
    totalSteps: Int,
    stepTitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 28.dp)
    ) {
        // Etiqueta de paso
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text  = "Paso $currentStep de $totalSteps",
                style = MaterialTheme.typography.labelMedium,
                color = HT_TextMuted
            )
            Text(
                text  = "${((currentStep.toFloat() / totalSteps) * 100).toInt()}%",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                ),
                color = HT_Navy
            )
        }

        Spacer(Modifier.height(8.dp))

        // Barra de progreso
        LinearProgressIndicator(
            progress   = { currentStep.toFloat() / totalSteps },
            modifier   = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color      = HT_Navy,
            trackColor = HT_Border
        )

        Spacer(Modifier.height(20.dp))

        // Título del paso
        Text(
            text  = stepTitle,
            style = MaterialTheme.typography.headlineLarge,
            color = HT_Navy
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════
//  HtCard
//  Tarjeta blanca con borde y sombra suave — contenedor de formularios
// ═══════════════════════════════════════════════════════════════════════════
@Composable
fun HtCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(20.dp),
        colors   = CardDefaults.cardColors(
            containerColor = HT_White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        content = content
    )
}

// ═══════════════════════════════════════════════════════════════════════════
//  HtLogoTitle
//  Logo textual con punto ámbar — firma visual de HelpTata
// ═══════════════════════════════════════════════════════════════════════════
@Composable
fun HtLogoTitle(
    modifier: Modifier = Modifier,
    onDarkBackground: Boolean = true
) {
    val textColor = if (onDarkBackground) HT_White else HT_Navy

    Row(
        modifier          = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Punto ámbar — firma de marca
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(HT_Amber, shape = RoundedCornerShape(50))
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text  = "HelpTata",
            style = MaterialTheme.typography.headlineMedium.copy(
                color      = textColor,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize   = 32.sp
            )
        )
    }
}
