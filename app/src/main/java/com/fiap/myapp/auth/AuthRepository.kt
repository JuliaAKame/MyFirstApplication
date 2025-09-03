package com.fiap.myapp.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * Repository responsável por gerenciar autenticação com Firebase
 */
class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    
    /**
     * Verifica se há um usuário logado atualmente
     */
    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser
    
    /**
     * Faz login com email e senha
     */
    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Erro desconhecido durante o login"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Registra novo usuário com email e senha
     */
    suspend fun register(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Erro desconhecido durante o cadastro"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Faz logout do usuário atual
     */
    fun logout() {
        firebaseAuth.signOut()
    }
    
    /**
     * Adiciona listener para mudanças no estado de autenticação
     */
    fun addAuthStateListener(listener: FirebaseAuth.AuthStateListener) {
        firebaseAuth.addAuthStateListener(listener)
    }
    
    /**
     * Remove listener de mudanças no estado de autenticação
     */
    fun removeAuthStateListener(listener: FirebaseAuth.AuthStateListener) {
        firebaseAuth.removeAuthStateListener(listener)
    }
}
