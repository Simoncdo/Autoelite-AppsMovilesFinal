package com.example.autoelite.data.model

data class Usuario(
    val id: Long? = null,
    val nombre: String? = "",
    val email: String? = "",
    val contrasena: String? = "",
    val direccion: String? = "",
    val rut: String? = "",
    val rol: String? = "CLIENTE"
)