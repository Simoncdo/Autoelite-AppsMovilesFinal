package com.example.autoelite.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.autoelite.data.model.Auto
import com.example.autoelite.viewmodel.AdminViewModel
import com.example.autoelite.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAutoScreen(
    navController: NavController,
    adminViewModel: AdminViewModel,
    autoId: String? = null
) {
    val isEditing = autoId != null
    val autoState by adminViewModel.autoState.collectAsState()

    val autoToEdit = if (isEditing && autoState is UiState.Success) {
        (autoState as UiState.Success<List<Auto>>).data.find { it.id == autoId?.toLong() }
    } else {
        null
    }

    var marca by remember { mutableStateOf(autoToEdit?.marca ?: "") }
    var modelo by remember { mutableStateOf(autoToEdit?.modelo ?: "") }
    var kilometraje by remember { mutableStateOf(autoToEdit?.kilometraje?.toString() ?: "") }
    var tipoAuto by remember { mutableStateOf(autoToEdit?.tipoAuto ?: "") }
    var esDeportivo by remember { mutableStateOf(autoToEdit?.esDeportivo ?: false) }
    var tipoMotor by remember { mutableStateOf(autoToEdit?.tipoMotor ?: "") }
    var precio by remember { mutableStateOf(autoToEdit?.precio?.toString() ?: "") }
    var imagenes by remember { mutableStateOf(autoToEdit?.imagenes?.joinToString(", ") ?: "") }

    LaunchedEffect(autoToEdit) {
        if (autoToEdit != null) {
            marca = autoToEdit.marca ?: ""
            modelo = autoToEdit.modelo ?: ""
            kilometraje = autoToEdit.kilometraje?.toString() ?: ""
            tipoAuto = autoToEdit.tipoAuto ?: ""
            esDeportivo = autoToEdit.esDeportivo ?: false
            tipoMotor = autoToEdit.tipoMotor ?: ""
            precio = autoToEdit.precio?.toString() ?: ""
            imagenes = autoToEdit.imagenes?.joinToString(", ") ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Auto" else "Añadir Auto") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(value = marca, onValueChange = { marca = it }, label = { Text("Marca") })
            OutlinedTextField(value = modelo, onValueChange = { modelo = it }, label = { Text("Modelo") })
            OutlinedTextField(value = kilometraje, onValueChange = { kilometraje = it }, label = { Text("Kilometraje") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
            OutlinedTextField(value = tipoAuto, onValueChange = { tipoAuto = it }, label = { Text("Tipo de Auto") })
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = esDeportivo, onCheckedChange = { esDeportivo = it })
                Text("¿Es deportivo?")
            }
            OutlinedTextField(value = tipoMotor, onValueChange = { tipoMotor = it }, label = { Text("Tipo de Motor") })
            OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            OutlinedTextField(value = imagenes, onValueChange = { imagenes = it }, label = { Text("Imágenes (URLs separadas por coma)") })

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = {
                val auto = Auto(
                    id = autoToEdit?.id,
                    marca = marca,
                    modelo = modelo,
                    kilometraje = kilometraje.toDoubleOrNull() ?: 0.0,
                    tipoAuto = tipoAuto,
                    esDeportivo = esDeportivo,
                    tipoMotor = tipoMotor,
                    precio = precio.toLongOrNull() ?: 0L,
                    imagenes = imagenes.split(",").map { it.trim() }
                )
                adminViewModel.saveAuto(auto) { success ->
                    if (success) navController.popBackStack()
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Guardar")
            }
        }
    }
}