package com.example.autoelite.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.rememberAsyncImagePainter
import com.example.autoelite.data.model.Auto
import com.example.autoelite.viewmodel.AuthViewModel
import com.example.autoelite.viewmodel.CarritoViewModel
import com.example.autoelite.viewmodel.ProductoViewModel
import com.example.autoelite.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoPantalla(
    navController: NavController, 
    authViewModel: AuthViewModel,
    productoViewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel
) {
    val usuario by authViewModel.usuarioActual
    LaunchedEffect(Unit) {
        productoViewModel.cargarAutos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        navController.navigate("inicio_sesion") {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("carrito") }) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Ver Carrito")
            }
        }
    ) { paddingValues ->
        val productosState by productoViewModel.productosState.collectAsState()

        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = productosState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        Text("No hay autos disponibles en este momento.")
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            items(state.data) { auto ->
                                Card(
                                    modifier = Modifier
                                        .padding(bottom = 16.dp)
                                        .clickable { 
                                            auto.id?.let { navController.navigate("detalle_producto/$it") } 
                                        },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Column {
                                        Image(
                                            painter = rememberAsyncImagePainter(auto.imagenes?.firstOrNull()),
                                            contentDescription = "${auto.marca ?: ""} ${auto.modelo ?: ""}",
                                            modifier = Modifier.height(200.dp).fillMaxWidth(),
                                            contentScale = ContentScale.Crop
                                        )
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(text = "${auto.marca ?: ""} ${auto.modelo ?: ""}", style = MaterialTheme.typography.titleLarge)
                                            
                                            val precioFormatted = String.format("%,d", auto.precio ?: 0L)
                                            Text(text = "$$precioFormatted", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                                            
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Button(onClick = { carritoViewModel.agregarAlCarrito(auto) }) {
                                                Text("Agregar al Carrito")
                                            }
                                        }
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