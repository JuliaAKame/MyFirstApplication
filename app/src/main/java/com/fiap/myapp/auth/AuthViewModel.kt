package com.fiap.myapp.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsável por gerenciar o estado de autenticação da aplicação
 */
class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        _authState.value = if (user != null) {
            AuthState.Authenticated(user)
        } else {
            AuthState.Unauthenticated
        }
    }
    
    init {
        // Adiciona listener para mudanças no estado de autenticação
        authRepository.addAuthStateListener(authStateListener)
        
        // Verifica estado inicial
        val currentUser = authRepository.getCurrentUser()
        _authState.value = if (currentUser != null) {
            AuthState.Authenticated(currentUser)
        } else {
            AuthState.Unauthenticated
        }
    }
    
    /**
     * Faz login com email e senha
     */
    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email e senha são obrigatórios")
            return
        }
        
        _authState.value = AuthState.Loading
        
        viewModelScope.launch {
            authRepository.login(email, password)
                .onSuccess { user ->
                    _authState.value = AuthState.Authenticated(user)
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(getErrorMessage(exception))
                }
        }
    }
    
    /**
     * Registra novo usuário
     */
    fun register(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email e senha são obrigatórios")
            return
        }
        
        _authState.value = AuthState.Loading
        
        viewModelScope.launch {
            authRepository.register(email, password)
                .onSuccess { user ->
                    _authState.value = AuthState.Authenticated(user)
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(getErrorMessage(exception))
                }
        }
    }
    
    /**
     * Faz logout do usuário atual
     */
    fun logout() {
        authRepository.logout()
    }
    
    /**
     * Limpa mensagens de erro
     */
    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }
    
    /**
     * Converte exceções Firebase em mensagens amigáveis
     */
    private fun getErrorMessage(exception: Throwable): String {
        return when (exception) {
            is FirebaseAuthInvalidUserException -> "Usuário não encontrado"
            is FirebaseAuthInvalidCredentialsException -> "Email ou senha incorretos"
            is FirebaseAuthUserCollisionException -> "Email já está em uso"
            is FirebaseAuthWeakPasswordException -> "Senha muito fraca. Use pelo menos 6 caracteres"
            else -> exception.message ?: "Erro desconhecido"
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        authRepository.removeAuthStateListener(authStateListener)
    }
}
