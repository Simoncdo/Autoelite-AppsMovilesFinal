package com.example.autoelite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autoelite.data.model.Auto
import com.example.autoelite.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {

    private val _productos = MutableStateFlow<List<Auto>>(emptyList())
    val productos: StateFlow<List<Auto>> = _productos

    init {
        cargarAutos()
    }

    fun cargarAutos() {
        viewModelScope.launch {
            try {
                val autosList = RetrofitClient.webService.getAutos()
                _productos.value = autosList
            } catch (e: Exception) {
                println("Error al cargar autos: ${e.message}")
                _productos.value = emptyList()
            }
        }
    }

    fun buscarPorId(id: Long): Auto? {
        return _productos.value.find { it.id == id }
    }
}