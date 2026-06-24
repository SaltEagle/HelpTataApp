package com.example.helptataapp.navegation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.helptataapp.ui.components.*
import com.example.helptataapp.ui.theme.*

/*
 * WelcomeScreen — Pantalla de inicio de HelpTata
 *
 * Es la primera pantalla que ve el usuario al abrir la app.
 * Presenta la marca, una descripción clara y dos botones de acción.
 *
 * Decisiones de diseño (Nielsen / WCAG / adultos mayores):
 *  • Fondo azul marino con gradiente → identidad visual HelpTata
 *  • Logo textual con punto ámbar → reconocible sin imagen externa
 *  • Texto grande (bodyLarge = 20sp) con alto contraste (blanco sobre marino)
 *  • Dos botones claramente diferenciados: ámbar (Registrarse) y borde blanco (Iniciar Sesión)
 *  • Botones de 60dp de alto → área táctil ≥ 48dp recomendada (WCAG 2.5.5)
 *  • Orden: Registrarse primero (acción principal para usuarios nuevos)
 *  • Sin distracciones visuales — diseño minimalista (Nielsen #8)
 */
@Composable
fun WelcomeScreen(
    navController: NavController
) {
    HtGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            verticalArrangement   = Arrangement.Center,
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {

            // ── Logo ────────────────────────────────────────────────────
            HtLogoTitle(
                modifier          = Modifier.fillMaxWidth(),
                onDarkBackground  = true
            )

            Spacer(Modifier.height(24.dp))

            // ── Tagline ─────────────────────────────────────────────────
            Text(
                text      = "Aprende tecnología\na tu propio ritmo",
                style     = MaterialTheme.typography.displayLarge.copy(
                    fontSize   = 38.sp,
                    lineHeight = 46.sp
                ),
                textAlign = TextAlign.Center,
                color     = HT_White
            )

            Spacer(Modifier.height(20.dp))

            // ── Descripción ─────────────────────────────────────────────
            Text(
                text  = "Cursos claros y sencillos diseñados\nespecialmente para adultos mayores.\nProtégete, conéctate y aprende sin apuro.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize   = 19.sp,
                    lineHeight = 28.sp
                ),
                textAlign = TextAlign.Center,
                color     = HT_White.copy(alpha = 0.85f)
            )

            Spacer(Modifier.height(52.dp))

            // ── CTA principal: Registrarse (ámbar) ──────────────────────
            HtAmberButton(
                text    = "Registrarse",
                onClick = { navController.navigate("run") }
            )

            Spacer(Modifier.height(16.dp))

            // ── CTA secundario: Iniciar sesión (borde blanco) ───────────
            HtOutlineButton(
                text    = "Iniciar Sesión",
                onClick = { navController.navigate("login") }
            )

            Spacer(Modifier.height(36.dp))

            // ── Pie informativo ─────────────────────────────────────────
            Text(
                text  = "© 2026 HelpTata · Accesible para todos",
                style = MaterialTheme.typography.labelMedium,
                color = HT_White.copy(alpha = 0.45f),
                textAlign = TextAlign.Center
            )
        }
    }
}
