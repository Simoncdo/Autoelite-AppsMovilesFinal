package com.example.autoelite.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.autoelite.util.esRutValido
import com.example.autoelite.viewmodel.AuthViewModel

@Composable
fun RegistroPantalla(navController: NavController, viewModel: AuthViewModel) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var esRutValido by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = rut,
            onValueChange = { 
                rut = it
                esRutValido = esRutValido(it)
            },
            label = { Text("RUT (con puntos y guion)") },
            isError = !esRutValido,
            modifier = Modifier.fillMaxWidth()
        )
        if (!esRutValido) {
            Text("RUT no válido", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.registrar(nombre, email, contrasena, direccion, rut) { success ->
                    if (success) {
                        navController.navigate("inicio_sesion")
                    }
                }
            },
            enabled = esRutValido && nombre.isNotBlank() && email.isNotBlank() && contrasena.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(viewModel.mensaje.value)
        TextButton(onClick = { navController.navigate("inicio_sesion") }) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}