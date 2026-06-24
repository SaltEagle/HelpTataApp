package com.example.helptataapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Tipografía HelpTata ────────────────────────────────────────────────────
// Tamaños aumentados vs. Material 3 por defecto para mejorar legibilidad
// en adultos mayores (WCAG 1.4.4, SENADIS, Gobierno Digital Chile).
//
// Escala usada:
//   displayLarge   → 57sp  (títulos hero grandes)
//   headlineLarge  → 36sp  (títulos de pantalla)
//   headlineMedium → 30sp  (subtítulos de sección)
//   titleLarge     → 26sp  (títulos de tarjeta)
//   bodyLarge      → 20sp  (texto principal — mínimo recomendado)
//   bodyMedium     → 18sp  (texto secundario)
//   labelLarge     → 18sp  (texto de botones)
//   labelMedium    → 16sp  (etiquetas de campo)

val HelpTataTypography = Typography(

    // Títulos hero (WelcomeScreen)
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize   = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = (-0.5).sp,
        color = HT_White
    ),

    // Título principal de cada pantalla
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize   = 34.sp,
        lineHeight = 42.sp,
        color      = HT_Navy
    ),

    // Subtítulos de sección
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize   = 28.sp,
        lineHeight = 36.sp,
        color      = HT_Navy
    ),

    // Títulos de tarjetas
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize   = 24.sp,
        lineHeight = 32.sp,
        color      = HT_TextMain
    ),

    // Texto de cuerpo principal — mínimo legible para adultos mayores
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 20.sp,
        lineHeight = 30.sp,
        color      = HT_TextMain
    ),

    // Texto secundario
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 18.sp,
        lineHeight = 28.sp,
        color      = HT_TextMid
    ),

    // Texto de botones — siempre grande y legible
    labelLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize   = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.1.sp
    ),

    // Etiquetas de campos de formulario
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 16.sp,
        lineHeight = 24.sp,
        color      = HT_TextMid
    ),
)
