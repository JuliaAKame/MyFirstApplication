package com.fiap.myapp.auth

import com.google.firebase.auth.FirebaseUser

/**
 * Estados possíveis da autenticação do usuário
 */
sealed class AuthState {
    /**
     * Estado inicial - verificando se há usuário logado
     */
    object Loading : AuthState()
    
    /**
     * Usuário autenticado com sucesso
     */
    data class Authenticated(val user: FirebaseUser) : AuthState()
    
    /**
     * Usuário não autenticado - precisa fazer login
     */
    object Unauthenticated : AuthState()
    
    /**
     * Erro durante processo de autenticação
     */
    data class Error(val message: String) : AuthState()
}
