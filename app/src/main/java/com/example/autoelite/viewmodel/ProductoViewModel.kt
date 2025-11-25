package com.example.autoelite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autoelite.data.model.Auto
import com.example.autoelite.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {

    // Cambiamos el StateFlow para que exponga un UiState
    private val _productosState = MutableStateFlow<UiState<List<Auto>>>(UiState.Loading)
    val productosState: StateFlow<UiState<List<Auto>>> = _productosState

    init {
        cargarAutos()
    }

    fun cargarAutos() {
        _productosState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val autosList = RetrofitClient.webService.getAutos()
                _productosState.value = UiState.Success(autosList)
            } catch (e: Exception) {
                _productosState.value = UiState.Error("Error al cargar el catálogo: ${e.message}")
            }
        }
    }

    // Esta función ahora debe buscar en el estado de éxito
    fun buscarPorId(id: Long): Auto? {
        return if (_productosState.value is UiState.Success) {
            (_productosState.value as UiState.Success<List<Auto>>).data.find { it.id == id }
        } else {
            null
        }
    }
}