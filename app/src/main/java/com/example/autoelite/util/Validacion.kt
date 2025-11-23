package com.example.autoelite.util
fun esRutValido(rut: String): Boolean {
    val rutLimpio = rut.replace(Regex("[.-]"), "")
    if (rutLimpio.length < 2) return false

    val cuerpo = rutLimpio.substring(0, rutLimpio.length - 1)
    val digitoVerificador = rutLimpio.last().uppercaseChar()

    if (!cuerpo.all { it.isDigit() }) return false

    try {
        var suma = 0
        var multiplo = 2
        for (i in cuerpo.reversed()) {
            suma += i.toString().toInt() * multiplo
            multiplo = if (multiplo == 7) 2 else multiplo + 1
        }
        val dvCalculado = when (val resultado = 11 - (suma % 11)) {
            11 -> '0'
            10 -> 'K'
            else -> resultado.toString().first()
        }
        return digitoVerificador == dvCalculado
    } catch (e: NumberFormatException) {
        return false
    }
}
