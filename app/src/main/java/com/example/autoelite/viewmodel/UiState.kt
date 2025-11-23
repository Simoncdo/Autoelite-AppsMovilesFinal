package com.example.autoelite.viewmodel

/**
 * Una sealed class para representar los estados de la UI (Cargando, Éxito, Error).
 * Es genérica para poder reutilizarla con cualquier tipo de dato.
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}