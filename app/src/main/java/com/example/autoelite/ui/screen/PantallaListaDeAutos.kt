package com.example.autoelite.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.autoelite.viewmodel.AdminViewModel
import com.example.autoelite.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoListScreen(navController: NavController, adminViewModel: AdminViewModel) {
    val autoState by adminViewModel.autoState.collectAsState()
    LaunchedEffect(Unit) {
        adminViewModel.loadAllAutos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Autos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("admin/autos/add") }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Auto")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = autoState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.data) { auto ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = "${auto.marca ?: ""} ${auto.modelo ?: ""}", style = MaterialTheme.typography.titleMedium)
                                        Text(text = "ID: ${auto.id?.toString() ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                                    }
                                    IconButton(onClick = { 
                                        auto.id?.let { navController.navigate("admin/autos/edit/$it") } 
                                    }) {
                                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                                    }
                                    IconButton(onClick = { 
                                        auto.id?.let { adminViewModel.deleteAuto(it) {} }
                                    }) {
                                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> Text(text = state.message)
            }
        }
    }
}