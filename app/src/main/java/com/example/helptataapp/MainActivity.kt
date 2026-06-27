package com.example.helptataapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.helptataapp.navegation.*
import com.example.helptataapp.session.AppSession
import com.example.helptataapp.session.TokenStore
import com.example.helptataapp.ui.theme.HelpTataAppTheme
import com.example.helptataapp.viewmodel.RegisterViewModel

/*
 * MainActivity  —  Flujo de registro con correo electrónico
 *
 * Flujo completo (7 pasos):
 *   1. run      → RUT completo (formato automático + validación módulo 11)
 *   2. nombre   → Primer y segundo nombre (solo letras)
 *   3. apellido → Primer y segundo apellido (solo letras)
 *   4. fecha    → Fecha de nacimiento (calendario, desde 1900, mín. 18 años)
 *   5. telefono → Celular chileno (9 dígitos, empieza en 9)
 *   6. correo   → Correo electrónico (validación + confirmación) ← NUEVO
 *   7. password → Contraseña (confirmación + medidor de fortaleza)
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HelpTataAppTheme {

                val navController = rememberNavController()
                val viewModel: RegisterViewModel = viewModel()
                val ctx = LocalContext.current

                // Restaurar sesión cifrada si existe; sin token → login directo
                val startDestination = remember {
                    val saved = TokenStore.load(ctx)
                    if (saved.isNotBlank()) {
                        AppSession.token = saved
                        "webview"
                    } else {
                        "login"
                    }
                }

                NavHost(
                    navController    = navController,
                    startDestination = startDestination
                ) {

                    // ── Pantallas de inicio ─────────────────────────────
                    composable("welcome") {
                        WelcomeScreen(navController)
                    }

                    composable("login") {
                        LoginScreen(navController)
                    }

                    // ── Flujo de registro (7 pasos) ─────────────────────

                    // Paso 1: RUT completo
                    composable("run") {
                        RunScreen(navController, viewModel)
                    }

                    // Paso 2: Nombre
                    composable("nombre") {
                        NombreScreen(navController, viewModel)
                    }

                    // Paso 3: Apellido
                    composable("apellido") {
                        ApellidoScreen(navController, viewModel)
                    }

                    // Paso 4: Fecha de nacimiento
                    composable("fecha") {
                        FechaScreen(navController, viewModel)
                    }

                    // Paso 5: Teléfono
                    composable("telefono") {
                        TelefonoScreen(navController, viewModel)
                    }

                    // Paso 6: Correo electrónico
                    composable("correo") {
                        CorreoScreen(navController, viewModel)
                    }

                    // Paso 7: Contraseña (llama a la API)
                    composable("password") {
                        PasswordScreen(navController, viewModel)
                    }

                    // Web app embebida (post-login)
                    composable("webview") {
                        WebAppScreen(
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
