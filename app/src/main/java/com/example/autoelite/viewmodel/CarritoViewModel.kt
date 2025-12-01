package com.example.autoelite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autoelite.data.model.Auto // Usamos el modelo correcto
import com.example.autoelite.data.remote.CurrencyClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CarritoItem(val auto: Auto, var cantidad: Int)

class CarritoViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<CarritoItem>>(emptyList())
    val items: StateFlow<List<CarritoItem>> = _items

    val total: Double
        get() = _items.value.sumOf { (it.auto.precio?.toDouble() ?: 0.0) * it.cantidad }
    private val _monedaSeleccionada = MutableStateFlow("CLP")
    val monedaSeleccionada: StateFlow<String> = _monedaSeleccionada

    private val _factorConversion = MutableStateFlow(1.0)
    val factorConversion: StateFlow<Double> = _factorConversion
    fun cambiarMoneda(nuevaMoneda: String) {
        viewModelScope.launch {
            try {
                if (nuevaMoneda == "CLP") {
                    _factorConversion.value = 1.0
                    _monedaSeleccionada.value = "CLP"
                } else {
                    // Llamamos a la API
                    val respuesta = CurrencyClient.service.obtenerTasas()
                    // Buscamos la tasa de la moneda que queremos (USD o EUR)
                    val tasa = respuesta.tasas[nuevaMoneda] ?: 1.0

                    _factorConversion.value = tasa
                    _monedaSeleccionada.value = nuevaMoneda
                }
            } catch (e: Exception) {
                // Si falla (ej. sin internet), volvemos a CLP
                _factorConversion.value = 1.0
                _monedaSeleccionada.value = "CLP"
                println("Error al obtener divisa: ${e.message}")
            }
        }
    }


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
        if (itemEncontrado != null) {
            if (cantidad > 0) {
                itemEncontrado.cantidad = cantidad
            } else {
                itemsActuales.remove(itemEncontrado)
            }
        }
        _items.value = itemsActuales
    }

    fun vaciarCarrito() {
        _items.value = emptyList()
    }
}