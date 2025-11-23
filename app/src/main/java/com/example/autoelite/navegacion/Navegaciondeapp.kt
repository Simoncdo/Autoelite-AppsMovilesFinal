package com.example.autoelite.navegacion

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autoelite.ui.screen.*
import com.example.autoelite.viewmodel.AdminViewModel
import com.example.autoelite.viewmodel.AuthViewModel
import com.example.autoelite.viewmodel.CarritoViewModel
import com.example.autoelite.viewmodel.ProductoViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()
    val carritoViewModel: CarritoViewModel = viewModel()
    val adminViewModel: AdminViewModel = viewModel()

    NavHost(navController = navController, startDestination = "registro") {
        composable("registro") { 
            RegistroPantalla(navController, authViewModel)
        }
        composable("inicio_sesion") { 
            LoginPantalla(navController, authViewModel)
        }
        composable("catalogo") { 
            CatalogoPantalla(navController, authViewModel, productoViewModel, carritoViewModel) 
        }
        composable("detalle_producto/{productoId}") { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString("productoId")
            DetalleProductoPantalla(navController, productoId, authViewModel, productoViewModel, carritoViewModel)
        }
        composable("carrito") { 
            CarritoPantalla(navController, carritoViewModel) 
        }
        composable("compra_exitosa") { 
            CompraExitosaPantalla(navController) 
        }
        composable("compra_rechazada") { 
            CompraRechazadaPantalla(navController) 
        }

        composable("back_office") { 
            BackOfficePantalla(navController, authViewModel, adminViewModel)
        }
        composable("admin/autos") { // Renombrado a "autos"
            AutoListScreen(navController, adminViewModel)
        }
        composable("admin/users") {
            UserListScreen(navController, adminViewModel)
        }
        composable("admin/autos/add") {
            AddEditAutoScreen(navController, adminViewModel)
        }
        composable("admin/autos/edit/{autoId}") { backStackEntry ->
            val autoId = backStackEntry.arguments?.getString("autoId")
            AddEditAutoScreen(navController, adminViewModel, autoId)
        }
    }
}