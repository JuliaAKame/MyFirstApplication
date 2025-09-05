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
import com.fiap.myapp.screens.RecyclingScreen

/**
 * Main navigation configuration for the application.
 * Handles authentication-based navigation and route protection.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    // Handle authentication state changes and automatic navigation
    LaunchedEffect(authState) {
        handleAuthStateChange(
            authState = authState,
            navController = navController,
            onError = { message ->
                errorMessage = message
                showErrorDialog = true
            }
        )
    }
    
    // Show loading screen when authentication is in progress
    when (authState) {
        is AuthState.Loading -> {
            LoadingScreen()
            return
        }
        else -> {
            // Continue with normal navigation
        }
    }
    
    // Error dialog for authentication issues
    if (showErrorDialog) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = {
                showErrorDialog = false
                authViewModel.clearError()
            }
        )
    }
    
    AppNavHost(
        navController = navController,
        authState = authState,
        authViewModel = authViewModel
    )
}

/**
 * Handles authentication state changes and performs appropriate navigation.
 */
private suspend fun handleAuthStateChange(
    authState: AuthState,
    navController: NavHostController,
    onError: (String) -> Unit
) {
    when (authState) {
        is AuthState.Authenticated -> {
            navigateToAuthenticated(navController)
        }
        is AuthState.Unauthenticated -> {
            navigateToUnauthenticated(navController)
        }
        is AuthState.Error -> {
            handleAuthError(authState, navController, onError)
        }
        is AuthState.Loading -> {
            // Loading state handled in main composable
        }
    }
}

/**
 * Navigates to authenticated section and clears auth screens from backstack.
 */
private fun navigateToAuthenticated(navController: NavHostController) {
    navController.navigate(Routes.RECYCLING) {
        popUpTo(Routes.LOGIN) { inclusive = true }
        popUpTo(Routes.REGISTER) { inclusive = true }
    }
}

/**
 * Navigates to unauthenticated section if not already there.
 */
private fun navigateToUnauthenticated(navController: NavHostController) {
    val currentRoute = navController.currentDestination?.route
    
    if (currentRoute !in Routes.publicRoutes) {
        navController.navigate(Routes.LOGIN) {
            popUpTo(Routes.RECYCLING) { inclusive = true }
            popUpTo(Routes.PARTNERS) { inclusive = true }
        }
    }
}

/**
 * Handles authentication errors and redirects if necessary.
 */
private fun handleAuthError(
    errorState: AuthState.Error,
    navController: NavHostController,
    onError: (String) -> Unit
) {
    onError(errorState.message)
    
    val currentRoute = navController.currentDestination?.route
    if (currentRoute in Routes.protectedRoutes) {
        navController.navigate(Routes.LOGIN) {
            popUpTo(Routes.RECYCLING) { inclusive = true }
            popUpTo(Routes.PARTNERS) { inclusive = true }
        }
    }
}

/**
 * Application navigation host with all route definitions.
 */
@Composable
private fun AppNavHost(
    navController: NavHostController,
    authState: AuthState,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = determineStartDestination(authState)
    ) {
        composable(Routes.LOGIN) {
            Login(
                authViewModel = authViewModel,
                onNavigateToCadastro = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }
        
        composable(Routes.REGISTER) {
            CadastroScreen(navController = navController)
        }
        
        composable(Routes.RECYCLING) {
            ProtectedRoute(authState = authState) {
                RecyclingScreen(
                    onNavigateToHistory = {
                        navController.navigate(Routes.RECYCLING_HISTORY)
                    },
                    onNavigateToPartners = {
                        navController.navigate(Routes.PARTNERS)
                    }
                )
            }
        }
        
        composable(Routes.PARTNERS) {
            ProtectedRoute(authState = authState) {
                PartnersScreen(
                    onBack = { 
                        navController.navigate(Routes.RECYCLING) {
                            popUpTo(Routes.PARTNERS) { inclusive = true }
                        }
                    },
                    onLogout = { authViewModel.signOut() }
                )
            }
        }
    }
}

/**
 * Determines the appropriate start destination based on authentication state.
 */
private fun determineStartDestination(authState: AuthState): String {
    return when (authState) {
        is AuthState.Authenticated -> Routes.RECYCLING
        else -> Routes.LOGIN
    }
}

/**
 * Protected route wrapper that only renders content for authenticated users.
 */
@Composable
private fun ProtectedRoute(
    authState: AuthState,
    content: @Composable () -> Unit
) {
    when (authState) {
        is AuthState.Authenticated -> content()
        else -> {
            // Redirection handled by LaunchedEffect in main NavGraph
        }
    }
}
