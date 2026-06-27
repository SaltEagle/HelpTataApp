package com.example.helptataapp.navegation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.helptataapp.viewmodel.LoginState
import com.example.helptataapp.viewmodel.LoginViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.helptataapp.session.AppSession
import com.example.helptataapp.session.TokenStore
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
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val loginState by loginViewModel.state.collectAsState()
    val context = LocalContext.current

    // Estado del formulario
    var identificacion  by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var showPassword    by remember { mutableStateOf(false) }
    var idError         by remember { mutableStateOf("") }
    var passError       by remember { mutableStateOf("") }
    var generalError    by remember { mutableStateOf("") }

    // Cuando login es exitoso → guardar token cifrado y navegar al WebView
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            val token = (loginState as LoginState.Success).response.token
            AppSession.token = token
            TokenStore.save(context, token)
            Toast.makeText(context, "¡Bienvenido! Sesión iniciada correctamente.", Toast.LENGTH_SHORT).show()
            navController.navigate("webview") {
                popUpTo("welcome") { inclusive = false }
            }
        }
        if (loginState is LoginState.Error) {
            generalError = (loginState as LoginState.Error).mensaje
        }
    }

    val isLoading = loginState is LoginState.Loading

    // Validación local básica
    fun validate(): Boolean {
        var ok = true
        if (identificacion.isBlank()) {
            idError = "Ingresa tu correo electrónico."
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

                    // correo
                    HtTextField(
                        value         = identificacion,
                        onValueChange = {
                            identificacion = it
                            if (idError.isNotEmpty()) idError = ""
                        },
                        label         = "Correo Electrónico",
                        placeholder   = "tu@email.com",
                        errorMessage  = idError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        )
                    )

                    // Campo contraseña con toggle de visibilidad
                    HtTextField(
                        value         = password,
                        onValueChange = {
                            password = it
                            if (passError.isNotEmpty()) passError = ""
                        },
                        label                = "Contraseña",
                        errorMessage         = passError,
                        visualTransformation = if (showPassword) VisualTransformation.None
                                               else PasswordVisualTransformation(),
                        keyboardOptions      = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Filled.VisibilityOff
                                                  else Icons.Filled.Visibility,
                                    contentDescription = if (showPassword) "Ocultar contraseña"
                                                         else "Mostrar contraseña"
                                )
                            }
                        }
                    )

                    Spacer(Modifier.height(4.dp))

                    // Error general del backend
                    if (generalError.isNotEmpty()) {
                        Text(
                            text  = generalError,
                            style = MaterialTheme.typography.bodyMedium,
                            color = androidx.compose.ui.graphics.Color(0xFFDC2626)
                        )
                    }

                    // Botón iniciar sesión
                    if (isLoading) {
                        CircularProgressIndicator(modifier = androidx.compose.ui.Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        HtPrimaryButton(
                            text    = "Iniciar Sesión",
                            onClick = {
                                generalError = ""
                                if (validate()) {
                                    loginViewModel.login(identificacion, password)
                                }
                            }
                        )
                    }

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
