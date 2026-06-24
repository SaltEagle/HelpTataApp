package com.example.helptataapp.navegation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.helptataapp.ui.components.*
import com.example.helptataapp.ui.theme.*

/*
 * LoginScreen — Inicio de sesión
 *
 * Accesibilidad y usabilidad:
 *  • Fondo gradiente azul marino → identidad visual coherente con WelcomeScreen
 *  • Formulario dentro de HtCard (tarjeta blanca) → contraste máximo sobre fondo oscuro
 *  • Validación inline en HtTextField (error visible sin alertas nativas)
 *  • autoCorrect desactivado para el campo RUN → evita correcciones no deseadas
 *  • KeyboardType.Email para correo → teclado adecuado en móvil
 *  • Botón "Volver" en la parte superior → control y libertad (Nielsen #3)
 *  • ScrollState → accesible en pantallas pequeñas o con teclado abierto
 *
 * NOTA: Esta pantalla es de ejemplo visual. La lógica de login con API
 * debe agregarse en el ViewModel correspondiente (LoginViewModel).
 * El campo de "identificación" puede ser RUN o email según la API.
 */
@Composable
fun LoginScreen(
    navController: NavController
) {
    // Estado del formulario
    var identificacion by remember { mutableStateOf("") }
    var password       by remember { mutableStateOf("") }
    var idError        by remember { mutableStateOf("") }
    var passError      by remember { mutableStateOf("") }

    // Validación local básica
    fun validate(): Boolean {
        var ok = true
        if (identificacion.isBlank()) {
            idError = "Ingresa tu RUN o correo electrónico."
            ok = false
        } else {
            idError = ""
        }
        if (password.isBlank()) {
            passError = "Ingresa tu contraseña."
            ok = false
        } else {
            passError = ""
        }
        return ok
    }

    HtGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement   = Arrangement.Top,
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {

            // ── Botón volver ──────────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Text(
                        text  = "← Volver",
                        style = MaterialTheme.typography.labelLarge,
                        color = HT_White
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Logo ─────────────────────────────────────────────────
            HtLogoTitle(
                modifier         = Modifier.fillMaxWidth(),
                onDarkBackground = true
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text      = "Bienvenido de vuelta",
                style     = MaterialTheme.typography.bodyLarge,
                color     = HT_White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            // ── Tarjeta del formulario ─────────────────────────────────
            HtCard {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical   = 28.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    Text(
                        text  = "Iniciar Sesión",
                        style = MaterialTheme.typography.headlineLarge
                    )

                    // Campo RUN / correo
                    HtTextField(
                        value         = identificacion,
                        onValueChange = {
                            identificacion = it
                            if (idError.isNotEmpty()) idError = ""
                        },
                        label         = "RUN o Correo Electrónico",
                        placeholder   = "12345678-9 o tu@email.com",
                        errorMessage  = idError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        )
                    )

                    // Campo contraseña
                    HtTextField(
                        value         = password,
                        onValueChange = {
                            password = it
                            if (passError.isNotEmpty()) passError = ""
                        },
                        label               = "Contraseña",
                        errorMessage        = passError,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions     = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        )
                    )

                    Spacer(Modifier.height(4.dp))

                    // Botón iniciar sesión
                    HtPrimaryButton(
                        text    = "Iniciar Sesión",
                        onClick = {
                            if (validate()) {
                                // TODO: llamar a LoginViewModel.login(identificacion, password)
                                // navController.navigate("home") { popUpTo("welcome") }
                            }
                        }
                    )

                    // Enlace a registro
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            text  = "¿No tienes cuenta? ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = HT_TextMid
                        )
                        TextButton(
                            onClick = {
                                navController.navigate("run") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text  = "Regístrate aquí",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color      = HT_Navy,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
