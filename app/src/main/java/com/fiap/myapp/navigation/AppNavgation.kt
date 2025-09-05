package com.fiap.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fiap.myapp.screens.Login
import com.fiap.myapp.screens.CadastroScreen
import com.fiap.myapp.screens.CuponsScreen
import com.fiap.myapp.viewmodel.CuponsViewModel




object Routes {
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
    const val CUPONS = "cupons"  // adicionando a rota para cupons, caso precise
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            Login(navController)
        }
        composable(Routes.CADASTRO) {
            CadastroScreen(navController)
        }
        composable(Routes.CUPONS) {
            CuponsScreen(viewModel = CuponsViewModel())
        }
    }
}
