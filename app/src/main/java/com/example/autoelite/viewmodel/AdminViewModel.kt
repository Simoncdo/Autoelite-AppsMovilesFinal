package com.example.autoelite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autoelite.data.model.Auto
import com.example.autoelite.data.model.Usuario
import com.example.autoelite.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val _autoState = MutableStateFlow<UiState<List<Auto>>>(UiState.Loading)
    val autoState: StateFlow<UiState<List<Auto>>> = _autoState

    private val _userState = MutableStateFlow<UiState<List<Usuario>>>(UiState.Loading)
    val userState: StateFlow<UiState<List<Usuario>>> = _userState

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        loadAllAutos()
        loadAllUsers()
    }

    fun loadAllAutos() {
        _autoState.value = UiState.Loading
        viewModelScope.launch {
            try {
                _autoState.value = UiState.Success(RetrofitClient.webService.getAutos())
            } catch (e: Exception) {
                _autoState.value = UiState.Error("Error al cargar autos: ${e.message}")
            }
        }
    }

    fun saveAuto(auto: Auto, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = if (auto.id == null) {
                    RetrofitClient.webService.createAuto(auto)
                } else {
                    RetrofitClient.webService.updateAuto(auto.id, auto)
                }
                if (response.isSuccessful) {
                    _message.value = "Auto guardado con éxito"
                    loadAllAutos()
                    onResult(true)
                } else {
                    _message.value = "Error al guardar el auto"
                    onResult(false)
                }
            } catch (e: Exception) {
                _message.value = "Error de red: ${e.message}"
                onResult(false)
            }
        }
    }

    fun deleteAuto(autoId: Long, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.webService.deleteAuto(autoId)
                if (response.isSuccessful) {
                    _message.value = "Auto eliminado con éxito"
                    loadAllAutos()
                    onResult(true)
                } else {
                    _message.value = "Error al eliminar el auto"
                    onResult(false)
                }
            } catch (e: Exception) {
                _message.value = "Error de red: ${e.message}"
                onResult(false)
            }
        }
    }

    fun loadAllUsers() {
        _userState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val users = RetrofitClient.webService.getUsers()
                _userState.value = UiState.Success(users)
            } catch (e: Exception) {
                _userState.value = UiState.Error("Error al cargar usuarios: ${e.message}")
            }
        }
    }

    fun deleteUser(userId: Long, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.webService.deleteUser(userId)
                if (response.isSuccessful) {
                    _message.value = "Usuario eliminado con éxito"
                    loadAllUsers()
                    onResult(true)
                } else {
                    _message.value = "Error al eliminar el usuario"
                    onResult(false)
                }
            } catch (e: Exception) {
                _message.value = "Error de red: ${e.message}"
                onResult(false)
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}