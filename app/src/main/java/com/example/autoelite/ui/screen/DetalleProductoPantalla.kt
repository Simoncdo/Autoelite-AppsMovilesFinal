package com.example.autoelite.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.autoelite.viewmodel.AuthViewModel
import com.example.autoelite.viewmodel.CarritoViewModel
import com.example.autoelite.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoPantalla(
    navController: NavController,
    productoId: String?,
    authViewModel: AuthViewModel,
    productoViewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel
) {
    val auto = productoId?.toLongOrNull()?.let { productoViewModel.buscarPorId(it) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(auto?.let { "${it.marca ?: ""} ${it.modelo ?: ""}" } ?: "Detalle del Auto") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            auto?.let { 
                Column(modifier = Modifier.padding(16.dp)) {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Image(
                            painter = rememberAsyncImagePainter(it.imagenes?.firstOrNull()),
                            contentDescription = null,
                            modifier = Modifier.height(300.dp).fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "${it.marca ?: ""} ${it.modelo ?: ""}", style = MaterialTheme.typography.headlineMedium)
                    
                    val precioFormatted = String.format("%,d", it.precio ?: 0L)
                    Text(
                        text = "$$precioFormatted",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Tipo: ${it.tipoAuto ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Motor: ${it.tipoMotor ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                    
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { carritoViewModel.agregarAlCarrito(it) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Agregar al Carrito", modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Auto no encontrado")
                }
            }
        }
    }
}