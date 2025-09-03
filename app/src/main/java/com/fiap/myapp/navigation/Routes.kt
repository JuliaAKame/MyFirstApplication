package com.fiap.myapp.navigation

/**
 * Navigation routes definition for the application.
 * Centralized route management following Clean Code principles.
 */
object Routes {
    // Authentication routes
    const val LOGIN = "login"
    const val REGISTER = "cadastro"
    
    // Main application routes
    const val PARTNERS = "partners"
    
    /**
     * Determines the initial route based on authentication state.
     * @param isAuthenticated Current user authentication status
     * @return The appropriate route string
     */
    fun getStartRoute(isAuthenticated: Boolean): String {
        return if (isAuthenticated) PARTNERS else LOGIN
    }
    
    /**
     * All available routes as a list for validation purposes.
     */
    val allRoutes = listOf(LOGIN, REGISTER, PARTNERS)
    
    /**
     * Routes that don't require authentication.
     */
    val publicRoutes = listOf(LOGIN, REGISTER)
    
    /**
     * Routes that require authentication.
     */
    val protectedRoutes = listOf(PARTNERS)
}
