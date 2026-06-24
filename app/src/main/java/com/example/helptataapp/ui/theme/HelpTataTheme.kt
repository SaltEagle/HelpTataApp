package com.example.helptataapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Paleta HelpTata ────────────────────────────────────────────────────────
// Mismos valores que el diseño web para consistencia visual entre plataformas.

val HT_Navy        = Color(0xFF1A3A6C)   // Azul marino principal
val HT_NavyDark    = Color(0xFF0F2548)   // Azul marino oscuro (hover / pressed)
val HT_NavyLight   = Color(0xFFEEF4FF)   // Azul muy claro (fondos de campos)
val HT_Amber       = Color(0xFFCBDFFF)   // Ámbar — CTA principal
val HT_AmberDark   = Color(0xFF547EC4)   // Ámbar oscuro (pressed)
val HT_Cream       = Color(0xFFF5F3EF)   // Fondo general
val HT_White       = Color(0xFFFFFFFF)
val HT_TextMain    = Color(0xFF1A1A2E)   // Texto principal
val HT_TextMid     = Color(0xFF444444)   // Texto secundario
val HT_TextMuted   = Color(0xFF777777)   // Texto auxiliar
val HT_Error       = Color(0xFFDC2626)   // Rojo error
val HT_Success     = Color(0xFF28A745)   // Verde éxito
val HT_Border      = Color(0xFFD0DAE8)   // Borde de campos

// ── Esquema de colores Material 3 ─────────────────────────────────────────
private val HelpTataColorScheme = lightColorScheme(

    // Colores primarios — botones principales, íconos activos
    primary          = HT_Navy,
    onPrimary        = HT_White,
    primaryContainer = HT_NavyLight,
    onPrimaryContainer = HT_Navy,

    // Colores secundarios — ámbar para CTAs de acento
    secondary          = HT_Amber,
    onSecondary        = HT_TextMain,
    secondaryContainer = Color(0xFFFFF3E0),
    onSecondaryContainer = Color(0xFF7C4A00),

    // Fondos
    background  = HT_Cream,
    onBackground = HT_TextMain,
    surface     = HT_White,
    onSurface   = HT_TextMain,
    surfaceVariant    = HT_NavyLight,
    onSurfaceVariant  = HT_TextMid,

    // Error
    error   = HT_Error,
    onError = HT_White,

    // Outline (bordes de OutlinedTextField, etc.)
    outline        = HT_Border,
    outlineVariant = HT_NavyLight,
)

// ── Tema principal ─────────────────────────────────────────────────────────
@Composable
fun HelpTataAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HelpTataColorScheme,
        typography  = HelpTataTypography,   // definida en Typography.kt
        content     = content
    )
}
