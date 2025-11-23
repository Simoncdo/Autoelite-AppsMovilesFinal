package com.example.autoelite.data.model
data class Auto(
    val id: Long? = null,
    val marca: String? = "",
    val modelo: String? = "",
    val kilometraje: Double? = 0.0,
    val tipoAuto: String? = "",
    val esDeportivo: Boolean? = false,
    val tipoMotor: String? = "",
    val precio: Long? = 0,
    val imagenes: List<String>? = emptyList()
)