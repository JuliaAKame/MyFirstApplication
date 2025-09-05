package com.fiap.myapp.recycling

import com.fiap.myapp.recycling.models.RecyclingItem

/**
 * Represents the different states of the recycling feature.
 * Using sealed class pattern for type-safe state management.
 */
sealed class RecyclingState {
    
    /**
     * Initial state when screen loads.
     */
    data object Idle : RecyclingState()
    
    /**
     * Loading state during operations.
     */
    data object Loading : RecyclingState()
    
    /**
     * Successfully submitted recycling item.
     * @param item The submitted recycling item
     * @param pointsEarned Points awarded for this submission
     */
    data class Success(
        val item: RecyclingItem,
        val pointsEarned: Int
    ) : RecyclingState()
    
    /**
     * Error state when submission fails.
     * @param message Error message to display
     * @param exception Optional exception for debugging
     */
    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : RecyclingState()
    
    /**
     * Validation failed state.
     * @param message Validation failure message
     * @param canRetry Whether user can retry submission
     */
    data class ValidationFailed(
        val message: String,
        val canRetry: Boolean = true
    ) : RecyclingState()
    
    /**
     * Photo capture state management.
     */
    data class PhotoCaptured(
        val photoUri: String
    ) : RecyclingState()
}

/**
 * Extension properties for convenient state checking.
 */
val RecyclingState.isLoading: Boolean
    get() = this is RecyclingState.Loading

val RecyclingState.isSuccess: Boolean
    get() = this is RecyclingState.Success

val RecyclingState.isError: Boolean
    get() = this is RecyclingState.Error

val RecyclingState.hasPhoto: Boolean
    get() = this is RecyclingState.PhotoCaptured

/**
 * Gets the error message if state is error, null otherwise.
 */
val RecyclingState.errorMessage: String?
    get() = when (this) {
        is RecyclingState.Error -> message
        is RecyclingState.ValidationFailed -> message
        else -> null
    }
