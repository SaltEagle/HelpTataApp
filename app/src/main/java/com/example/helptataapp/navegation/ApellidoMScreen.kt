package com.example.helptataapp.navegation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.helptataapp.viewmodel.RegisterViewModel


@Composable
fun ApellidoMScreen(

    navController: NavController,
    viewModel: RegisterViewModel

) {

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement =
            Arrangement.Center

    ) {

        Text(

            text = "Ingrese su apellido materno",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium

        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        OutlinedTextField(

            value =
                viewModel
                    .sapellido_usuario
                    .value,

            onValueChange = {

                viewModel
                    .sapellido_usuario
                    .value = it

            },

            label = {
                Text("Apellido")
            },

            modifier =
                Modifier.fillMaxWidth()

        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(

            onClick = {

                navController.navigate(
                    "fecha"
                )

            },

            modifier =
                Modifier.fillMaxWidth()

        ) {

            Text("Continuar")

        }

    }

}