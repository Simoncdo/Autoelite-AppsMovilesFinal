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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.autoelite.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoPantalla(
    navController: NavController,
    viewModel: CarritoViewModel = viewModel()
) {
    val items by viewModel.items.collectAsState()
    val monedaActual by viewModel.monedaSeleccionada.collectAsState()
    val factor by viewModel.factorConversion.collectAsState()
    val totalEnCLP = viewModel.total
    val totalConvertido = totalEnCLP * factor

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
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total a Pagar:", style = MaterialTheme.typography.titleMedium)
                            val formato = when (monedaActual) {
                                "USD" -> NumberFormat.getCurrencyInstance(Locale.US)
                                "EUR" -> NumberFormat.getCurrencyInstance(Locale.GERMANY)
                                else -> NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                            }

                            Text(
                                text = formato.format(totalConvertido),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            BotonMoneda("CLP", monedaActual) { viewModel.cambiarMoneda("CLP") }
                            BotonMoneda("USD", monedaActual) { viewModel.cambiarMoneda("USD") }
                            BotonMoneda("EUR", monedaActual) { viewModel.cambiarMoneda("EUR") }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.vaciarCarrito()
                            navController.navigate("compra_exitosa")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = totalEnCLP > 0
                    ) {
                        Text("Confirmar Compra")
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tu carrito está vacío", style = MaterialTheme.typography.titleMedium)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(items) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${item.auto.marca ?: ""} ${item.auto.modelo ?: ""}", modifier = Modifier.weight(1f))
                            Text(text = "x${item.cantidad}", modifier = Modifier.padding(horizontal = 16.dp))
                            OutlinedButton(onClick = { viewModel.removerDelCarrito(item) }) {
                                Text("Eliminar")
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    }
}
@Composable
fun BotonMoneda(moneda: String, monedaActual: String, onClick: () -> Unit) {
    val seleccionado = moneda == monedaActual
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (seleccionado) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = moneda,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
        )
    }
}
