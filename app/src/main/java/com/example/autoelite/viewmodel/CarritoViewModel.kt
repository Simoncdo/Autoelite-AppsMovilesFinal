package com.example.autoelite.viewmodel

import androidx.lifecycle.ViewModel
import com.example.autoelite.data.model.Auto // Usamos el modelo correcto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Volvemos al nombre original, pero usando el modelo Auto
data class CarritoItem(val auto: Auto, var cantidad: Int)

class CarritoViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<CarritoItem>>(emptyList())
    val items: StateFlow<List<CarritoItem>> = _items

    val total: Double
        get() = _items.value.sumOf { (it.auto.precio?.toDouble() ?: 0.0) * it.cantidad }

    fun agregarAlCarrito(auto: Auto) {
        val itemsActuales = _items.value.toMutableList()
        val itemExistente = itemsActuales.find { it.auto.id == auto.id }

        if (itemExistente != null) {
            itemExistente.cantidad++
        } else {
            itemsActuales.add(CarritoItem(auto, 1))
        }
        _items.value = itemsActuales
    }

    fun removerDelCarrito(item: CarritoItem) {
        val itemsActuales = _items.value.toMutableList()
        itemsActuales.remove(item)
        _items.value = itemsActuales
    }

    fun actualizarCantidad(item: CarritoItem, cantidad: Int) {
        val itemsActuales = _items.value.toMutableList()
        val itemEncontrado = itemsActuales.find { it.auto.id == item.auto.id }
        if (itemEncontrado != null && cantidad > 0) {
            itemEncontrado.cantidad = cantidad
        } else if (cantidad <= 0) {
            removerDelCarrito(item)
        }
        _items.value = itemsActuales
    }

    fun vaciarCarrito() {
        _items.value = emptyList()
    }
}