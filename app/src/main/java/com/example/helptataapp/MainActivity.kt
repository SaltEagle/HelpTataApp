package com.example.helptataapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.helptataapp.navegation.ApellidoScreen
import com.example.helptataapp.navegation.DvRunScreen
import com.example.helptataapp.navegation.FechaScreen
import com.example.helptataapp.navegation.NombreScreen
import com.example.helptataapp.navegation.PasswordScreen
import com.example.helptataapp.navegation.RunScreen
import com.example.helptataapp.navegation.TelefonoScreen
import com.example.helptataapp.ui.theme.HelpTataAppTheme
import com.example.helptataapp.viewmodel.RegisterViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContent {

            val navController =
                rememberNavController()

            val viewModel:
                    RegisterViewModel =
                viewModel()

            NavHost(

                navController =
                    navController,

                startDestination =
                    "run"

            ) {

                composable("run") {

                    RunScreen(
                        navController,
                        viewModel
                    )

                }


                composable("dvrun") {

                    DvRunScreen(
                        navController,
                        viewModel
                    )

                }

                composable("nombre") {

                    NombreScreen(
                        navController,
                        viewModel
                    )

                }

                composable("apellido") {

                    ApellidoScreen(
                        navController,
                        viewModel
                    )

                }

                composable("fecha") {

                    FechaScreen(
                        navController,
                        viewModel
                    )

                }

                composable("telefono") {

                    TelefonoScreen(
                        navController,
                        viewModel
                    )

                }

                composable("password") {

                    PasswordScreen(
                        viewModel
                    )

                }

            }

        }

    }

}