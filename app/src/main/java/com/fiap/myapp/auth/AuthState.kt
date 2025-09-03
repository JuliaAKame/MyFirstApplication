package com.fiap.myapp.auth

import com.google.firebase.auth.FirebaseUser

/**
 * Represents all possible authentication states in the application.
 * Using sealed class ensures exhaustive when() statements and type safety.
 */
sealed class AuthState {
    /**
     * Initial state while checking existing authentication.
     * Displayed during app startup or authentication operations.
     */
    data object Loading : AuthState()
    
    /**
     * User is successfully authenticated and has active session.
     * @param user Firebase user instance with authentication details
     */
    data class Authenticated(val user: FirebaseUser) : AuthState()
    
    /**
     * User is not authenticated and needs to sign in.
     * Default state for new users or after logout.
     */
    data object Unauthenticated : AuthState()
    
    /**
     * Authentication process failed with specific error.
     * @param message Human-readable error message for user display
     */
    data class Error(val message: String) : AuthState()
    
    /**
     * Convenience property to check if user is currently authenticated.
     */
    val isAuthenticated: Boolean
        get() = this is Authenticated
        
    /**
     * Convenience property to check if authentication is in progress.
     */
    val isLoading: Boolean
        get() = this is Loading
        
    /**
     * Safely gets the authenticated user or null if not authenticated.
     */
    val userOrNull: FirebaseUser?
        get() = (this as? Authenticated)?.user
}
