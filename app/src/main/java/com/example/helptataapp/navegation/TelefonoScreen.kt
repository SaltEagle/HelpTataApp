package com.example.helptataapp.navegation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.helptataapp.viewmodel.RegisterViewModel

@Composable
fun TelefonoScreen(

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

            text = "Ingrese su teléfono",

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
                    .telefono_usuario
                    .value,

            onValueChange = {

                viewModel
                    .telefono_usuario
                    .value = it

            },

            label = {
                Text("912345678")
            },

            modifier =
                Modifier.fillMaxWidth(),

            keyboardOptions = KeyboardOptions(
                keyboardType =
                    KeyboardType.Phone
            )

        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(

            onClick = {

                navController.navigate(
                    "password"
                )

            },

            modifier =
                Modifier.fillMaxWidth()

        ) {

            Text("Continuar")

        }

    }

}