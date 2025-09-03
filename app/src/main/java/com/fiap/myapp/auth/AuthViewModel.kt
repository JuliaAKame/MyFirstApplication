package com.fiap.myapp.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing authentication state throughout the application.
 * Provides reactive authentication state and handles all auth operations.
 */
class AuthViewModel(
    private val authRepository: AuthRepository = FirebaseAuthRepository()
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        handleFirebaseAuthStateChange(firebaseAuth.currentUser)
    }
    
    companion object {
        private const val EMPTY_CREDENTIALS_ERROR = "Email and password are required"
        private const val SIGN_IN_ERROR_PREFIX = "Sign in failed: "
        private const val SIGN_UP_ERROR_PREFIX = "Registration failed: "
    }
    
    init {
        initializeAuthState()
    }
    
    /**
     * Initializes authentication state and sets up Firebase auth listener.
     */
    private fun initializeAuthState() {
        authRepository.addAuthStateListener(authStateListener)
        
        val currentUser = authRepository.getCurrentUser()
        _authState.value = if (currentUser != null) {
            AuthState.Authenticated(currentUser)
        } else {
            AuthState.Unauthenticated
        }
    }
    
    /**
     * Handles Firebase authentication state changes.
     */
    private fun handleFirebaseAuthStateChange(user: com.google.firebase.auth.FirebaseUser?) {
        _authState.value = if (user != null) {
            AuthState.Authenticated(user)
        } else {
            AuthState.Unauthenticated
        }
    }
    
    /**
     * Signs in user with email and password.
     * @param email User's email address
     * @param password User's password
     */
    fun signIn(email: String, password: String) {
        if (!validateCredentials(email, password)) return
        
        executeAuthOperation {
            authRepository.signIn(email, password)
        }
    }
    
    /**
     * Registers new user with email and password.
     * @param email User's email address  
     * @param password User's password
     */
    fun signUp(email: String, password: String) {
        if (!validateCredentials(email, password)) return
        
        executeAuthOperation {
            authRepository.signUp(email, password)
        }
    }
    
    /**
     * Signs out the current user.
     */
    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
                .onFailure { exception ->
                    _authState.value = AuthState.Error("Sign out failed: ${exception.message}")
                }
        }
    }
    
    /**
     * Clears any error state and returns to unauthenticated state.
     */
    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }
    
    /**
     * Validates that email and password are not blank.
     * @param email Email to validate
     * @param password Password to validate
     * @return true if valid, false otherwise
     */
    private fun validateCredentials(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error(EMPTY_CREDENTIALS_ERROR)
            return false
        }
        return true
    }
    
    /**
     * Executes authentication operation with proper state management.
     * @param operation Suspend function that returns authentication result
     */
    private fun executeAuthOperation(
        operation: suspend () -> Result<com.google.firebase.auth.FirebaseUser>
    ) {
        _authState.value = AuthState.Loading
        
        viewModelScope.launch {
            operation()
                .onSuccess { user ->
                    _authState.value = AuthState.Authenticated(user)
                }
                .onFailure { exception ->
                    val errorMessage = exception.message ?: "Unknown error occurred"
                    _authState.value = AuthState.Error(errorMessage)
                }
        }
    }
    
    /**
     * Cleanup when ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        authRepository.removeAuthStateListener(authStateListener)
    }
    
    // Legacy method names for backward compatibility
    @Deprecated("Use signIn instead", ReplaceWith("signIn(email, password)"))
    fun login(email: String, password: String) = signIn(email, password)
    
    @Deprecated("Use signUp instead", ReplaceWith("signUp(email, password)"))
    fun register(email: String, password: String) = signUp(email, password)
    
    @Deprecated("Use signOut instead", ReplaceWith("signOut()"))
    fun logout() = signOut()
}
