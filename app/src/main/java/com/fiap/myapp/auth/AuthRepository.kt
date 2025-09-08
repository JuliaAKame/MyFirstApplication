package com.fiap.myapp.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * Repository interface for authentication operations.
 * Abstracts Firebase authentication implementation details.
 */
interface AuthRepository {
    fun getCurrentUser(): FirebaseUser?
    suspend fun signIn(email: String, password: String): Result<FirebaseUser>
    suspend fun signUp(email: String, password: String): Result<FirebaseUser>
    suspend fun signOut(): Result<Unit>
    fun addAuthStateListener(listener: FirebaseAuth.AuthStateListener)
    fun removeAuthStateListener(listener: FirebaseAuth.AuthStateListener)
}

/**
 * Firebase implementation of AuthRepository.
 * Handles all Firebase Authentication operations with proper error handling.
 */
class FirebaseAuthRepository : AuthRepository {
    
    private val firebaseAuth = FirebaseAuth.getInstance()
    
    companion object {
    private const val UNKNOWN_LOGIN_ERROR = "Erro desconhecido ao fazer login"
    private const val UNKNOWN_REGISTER_ERROR = "Erro desconhecido ao registrar"
    private const val LOGOUT_ERROR = "Falha ao sair da conta"
    }
    
    /**
     * Gets the currently authenticated user.
     * @return FirebaseUser if authenticated, null otherwise
     */
    override fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser
    
    /**
     * Signs in user with email and password.
     * @param email User's email address
     * @param password User's password
     * @return Result containing FirebaseUser on success or exception on failure
     */
    override suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception(UNKNOWN_LOGIN_ERROR))
            }
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception(getFirebaseErrorMessage(e)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Creates new user account with email and password.
     * @param email User's email address
     * @param password User's password
     * @return Result containing FirebaseUser on success or exception on failure
     */
    override suspend fun signUp(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception(UNKNOWN_REGISTER_ERROR))
            }
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception(getFirebaseErrorMessage(e)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Signs out the current user.
     * @return Result indicating success or failure of logout operation
     */
    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(LOGOUT_ERROR, e))
        }
    }
    
    /**
     * Adds listener for authentication state changes.
     * @param listener Firebase auth state listener
     */
    override fun addAuthStateListener(listener: FirebaseAuth.AuthStateListener) {
        firebaseAuth.addAuthStateListener(listener)
    }
    
    /**
     * Removes authentication state listener.
     * @param listener Firebase auth state listener to remove
     */
    override fun removeAuthStateListener(listener: FirebaseAuth.AuthStateListener) {
        firebaseAuth.removeAuthStateListener(listener)
    }
    
    /**
     * Converts Firebase authentication errors to user-friendly messages.
     * @param exception FirebaseAuthException to convert
     * @return User-friendly error message
     */
    private fun getFirebaseErrorMessage(exception: FirebaseAuthException): String {
        return when (exception.errorCode) {
            "ERROR_INVALID_EMAIL" -> "Formato de e-mail inválido"
            "ERROR_WRONG_PASSWORD" -> "Senha incorreta"
            "ERROR_USER_NOT_FOUND" -> "Nenhuma conta encontrada com esse e-mail"
            "ERROR_USER_DISABLED" -> "Esta conta foi desativada"
            "ERROR_TOO_MANY_REQUESTS" -> "Muitas tentativas falhas. Tente novamente mais tarde"
            "ERROR_EMAIL_ALREADY_IN_USE" -> "E-mail já cadastrado"
            "ERROR_WEAK_PASSWORD" -> "Senha fraca. Use pelo menos 6 caracteres"
            "ERROR_NETWORK_REQUEST_FAILED" -> "Erro de conexão. Verifique sua internet"
            else -> exception.message ?: "Falha na autenticação"
        }
    }
}
