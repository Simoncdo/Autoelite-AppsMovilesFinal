package com.example.autoelite.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.autoelite.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoPantalla(navController: NavController, carritoViewModel: CarritoViewModel = viewModel()) {
    val items by carritoViewModel.items.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tu carrito está vacío", style = MaterialTheme.typography.titleMedium)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(items) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${item.auto.marca ?: ""} ${item.auto.modelo ?: ""}", modifier = Modifier.weight(1f))
                            Text(text = "x${item.cantidad}", modifier = Modifier.padding(horizontal = 16.dp))
                            OutlinedButton(onClick = { carritoViewModel.removerDelCarrito(item) }) {
                                Text("Eliminar")
                            }
                        }
                        Divider()
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                val totalFormatted = String.format("%,.0f", carritoViewModel.total)
                Text(
                    text = "Total: $$totalFormatted",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            carritoViewModel.vaciarCarrito()
                            navController.navigate("compra_exitosa")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(text = "Pagar", modifier = Modifier.padding(vertical = 8.dp))
                    }

                    Button(
                        onClick = {
                            navController.navigate("compra_rechazada")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Simular Rechazo de Pago", modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}