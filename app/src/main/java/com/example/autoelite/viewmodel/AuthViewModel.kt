package com.example.autoelite.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autoelite.data.model.Usuario
import com.example.autoelite.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    var mensaje = mutableStateOf("")
        private set

    var usuarioActual = mutableStateOf<Usuario?>(null)
        private set

    var esAdmin = mutableStateOf(false)
        private set

    fun registrar(nombre: String, email: String, contrasena: String, direccion: String, rut: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (nombre.isBlank() || email.isBlank() || contrasena.isBlank() || direccion.isBlank() || rut.isBlank()) {
                mensaje.value = "Todos los campos son obligatorios."
                onResult(false)
                return@launch
            }
            if (!email.contains("@") || !email.contains(".")) {
                mensaje.value = "Por favor, ingrese un correo electrónico válido."
                onResult(false)
                return@launch
            }
            if (contrasena.length < 6) {
                mensaje.value = "La contraseña debe tener al menos 6 caracteres."
                onResult(false)
                return@launch
            }

            val nuevoUsuario = Usuario(nombre = nombre, email = email, contrasena = contrasena, direccion = direccion, rut = rut)

            try {
                val response = RetrofitClient.webService.registrarUsuario(nuevoUsuario)
                if (response.isSuccessful) {
                    mensaje.value = "¡Registro exitoso! Ya puedes iniciar sesión."
                    onResult(true)
                } else {
                    mensaje.value = "El correo electrónico o RUT ya están registrados."
                    onResult(false)
                }
            } catch (e: Exception) {
                mensaje.value = "Error de red: ${e.message}"
                onResult(false)
            }
        }
    }

    fun login(email: String, contrasena: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (email == "admin@autoelite.com" && contrasena == "admin1234") {
                usuarioActual.value = Usuario(id = -1, nombre = "Admin", email = email, contrasena = "", direccion = "", rut = "", rol = "ADMIN")
                esAdmin.value = true
                mensaje.value = "Inicio de sesión de administrador exitoso."
                onResult(true)
                return@launch
            }

            esAdmin.value = false
            try {
                val response = RetrofitClient.webService.login(mapOf("email" to email, "contrasena" to contrasena))
                if (response.isSuccessful) {
                    val usuario = response.body()
                    if (usuario != null) {
                        usuarioActual.value = usuario
                        esAdmin.value = usuario.rol == "ADMIN"
                        mensaje.value = "Inicio de sesión exitoso. ¡Bienvenido, ${usuario.nombre}!"
                        onResult(true)
                    } else {
                        mensaje.value = "La respuesta del servidor fue vacía."
                        onResult(false)
                    }
                } else {
                    mensaje.value = "Credenciales inválidas. Por favor, inténtalo de nuevo."
                    onResult(false)
                }
            } catch (e: Exception) {
                mensaje.value = "Error de red: ${e.message}"
                onResult(false)
            }
        }
    }

    fun logout() {
        usuarioActual.value = null
        esAdmin.value = false
        mensaje.value = ""
    }
}