package com.fiap.myapp.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fiap.myapp.auth.AuthState
import com.fiap.myapp.auth.AuthViewModel
import com.fiap.myapp.screens.CadastroScreen
import com.fiap.myapp.screens.Login
import com.fiap.myapp.screens.PartnersScreen

/**
 * Configuração principal da navegação da aplicação
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    
    // Estado para controlar exibição de erros
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    // Observa mudanças no estado de autenticação e navega automaticamente
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                // Usuário logado - navega para Partners e limpa backstack
                navController.navigate(Routes.PARTNERS) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                    popUpTo(Routes.CADASTRO) { inclusive = true }
                }
            }
            is AuthState.Unauthenticated -> {
                // Usuário não logado - navega para Login se não estiver lá
                if (navController.currentDestination?.route != Routes.LOGIN &&
                    navController.currentDestination?.route != Routes.CADASTRO) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.PARTNERS) { inclusive = true }
                    }
                }
            }
            is AuthState.Error -> {
                // Mostra erro e garante que está na tela de login
                val errorState = authState as AuthState.Error
                errorMessage = errorState.message
                showErrorDialog = true
                if (navController.currentDestination?.route == Routes.PARTNERS) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.PARTNERS) { inclusive = true }
                    }
                }
            }
            is AuthState.Loading -> {
                // Estado de loading - mostra tela de loading
            }
        }
    }
    
    // Mostra loading se necessário
    when (authState) {
        is AuthState.Loading -> {
            LoadingScreen()
            return
        }
        else -> {
            // Continua com navegação normal
        }
    }
    
    // Dialog de erro
    if (showErrorDialog) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = {
                showErrorDialog = false
                authViewModel.clearError()
            }
        )
    }
    
    NavHost(
        navController = navController,
        startDestination = when (authState) {
            is AuthState.Authenticated -> Routes.PARTNERS
            else -> Routes.LOGIN
        }
    ) {
        composable(Routes.LOGIN) {
            Login(
                authViewModel = authViewModel,
                onNavigateToCadastro = {
                    navController.navigate(Routes.CADASTRO)
                }
            )
        }
        
        composable(Routes.CADASTRO) {
            CadastroScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.CADASTRO) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Routes.PARTNERS) {
            // Rota protegida - só acessível se autenticado
            when (authState) {
                is AuthState.Authenticated -> {
                    PartnersScreen(
                        onBack = {
                            // Partners é tela principal, faz logout
                            authViewModel.logout()
                        },
                        onLogout = {
                            authViewModel.logout()
                        }
                    )
                }
                else -> {
                    // Se não autenticado, redireciona automaticamente via LaunchedEffect
                    // Esta tela não será mostrada
                }
            }
        }
    }
}
