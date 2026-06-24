package com.example.helptataapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.helptataapp.navegation.*
import com.example.helptataapp.ui.theme.HelpTataAppTheme
import com.example.helptataapp.viewmodel.RegisterViewModel

/*
 * MainActivity
 *
 * Cambios respecto al original:
 *  • startDestination cambiado de "run" a "welcome"
 *    → El flujo ahora empieza en la pantalla de bienvenida
 *  • Se agrega la ruta "welcome" → WelcomeScreen
 *  • Se agrega la ruta "login"   → LoginScreen
 *  • enableEdgeToEdge() activado → la app usa toda la pantalla (mejor en móviles)
 *  • Todo envuelto en HelpTataAppTheme → aplica colores y tipografía HelpTata
 *
 * Flujo de navegación:
 *   welcome → login (si ya tiene cuenta)
 *   welcome → run → dvrun → nombre → apellido → fecha → telefono → password
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()   // pantalla completa, sin barras del sistema visibles

        setContent {
            HelpTataAppTheme {

                val navController = rememberNavController()
                val viewModel: RegisterViewModel = viewModel()

                NavHost(
                    navController    = navController,
                    startDestination = "welcome"      // ← punto de entrada
                ) {

                    // ── Pantallas nuevas ────────────────────────────────
                    composable("welcome") {
                        WelcomeScreen(navController)
                    }

                    composable("login") {
                        LoginScreen(navController)
                    }

                    // ── Flujo de registro (7 pasos) ─────────────────────
                    composable("run") {
                        RunScreen(navController, viewModel)
                    }

                    composable("dvrun") {
                        DvRunScreen(navController, viewModel)
                    }

                    composable("nombre") {
                        NombreScreen(navController, viewModel)
                    }

                    composable("apellido") {
                        ApellidoScreen(navController, viewModel)
                    }

                    composable("fecha") {
                        FechaScreen(navController, viewModel)
                    }

                    composable("telefono") {
                        TelefonoScreen(navController, viewModel)
                    }

                    composable("password") {
                        PasswordScreen(viewModel)
                    }
                }
            }
        }
    }
}
