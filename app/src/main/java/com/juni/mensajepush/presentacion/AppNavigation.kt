package com.juni.mensajepush.presentacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.juni.mensajepush.presentacion.autenticacion.AutenticacionPantalla
import com.juni.mensajepush.presentacion.autenticacion.AutenticacionViewModel
import com.juni.mensajepush.presentacion.conversacion.ConversacionPantalla
import com.juni.mensajepush.presentacion.conversacion.ConversacionViewModel
import com.juni.mensajepush.utilidades.AppContainer
import com.juni.mensajepush.utilidades.ViewModelFactory

@Composable
fun AppNavigation(appContainer: AppContainer) {
    val navController = rememberNavController()
    val viewModelFactory = remember { ViewModelFactory(appContainer) }
    
    // Determinar destino inicial
    val usuarioLogueado = appContainer.repositorioAutenticacion.obtenerUsuarioActual() != null
    val startDestination = if (usuarioLogueado) "conversacion" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            val viewModel: AutenticacionViewModel = viewModel(factory = viewModelFactory)
            AutenticacionPantalla(
                viewModel = viewModel,
                onAutenticacionExitosa = {
                    navController.navigate("conversacion") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("conversacion") {
            val viewModel: ConversacionViewModel = viewModel(factory = viewModelFactory)
            ConversacionPantalla(
                viewModel = viewModel,
                onCerrarSesion = {
                    navController.navigate("login") {
                        popUpTo("conversacion") { inclusive = true }
                    }
                }
            )
        }
    }
}
