package com.fiap.myapp.recycling.models

import java.util.Date
import java.util.UUID

/**
 * Represents a recyclable item registered by a user.
 * Contains all information about the recycling submission.
 */
data class RecyclingItem(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val photoUri: String,
    val description: String,
    val category: MaterialCategory,
    val weightRange: WeightRange,
    val pointsEarned: Int,
    val isValidated: Boolean = false,
    val validationMessage: String? = null,
    val createdAt: Date = Date(),
    val validatedAt: Date? = null
) {
    /**
     * Validates if the recycling item meets all requirements.
     * @return Pair of validation result and message
     */
    fun validate(): Pair<Boolean, String> {
        // Check minimum description length
        if (description.length < 10) {
            return false to "Descrição deve ter pelo menos 10 caracteres"
        }
        
        // Check if description matches category keywords
        if (!category.validateDescription(description)) {
            return false to "Descrição não corresponde à categoria selecionada"
        }
        
        // Check if photo exists (in real implementation, would verify file)
        if (photoUri.isBlank()) {
            return false to "Foto é obrigatória"
        }
        
        return true to "Item válido para pontuação"
    }
    
    /**
     * Creates a validated copy of this item with validation info.
     * @param isValid Whether the validation passed
     * @param message Validation message
     * @return Updated recycling item
     */
    fun withValidation(isValid: Boolean, message: String): RecyclingItem {
        return copy(
            isValidated = isValid,
            validationMessage = message,
            validatedAt = Date(),
            pointsEarned = if (isValid) pointsEarned else 0
        )
    }
    
    companion object {
        /**
         * Creates a new recycling item with calculated points.
         * @param userId User ID who submitted the item
         * @param photoUri URI of the captured photo
         * @param description User description
         * @param category Selected material category
         * @param weightRange Selected weight range
         * @return New recycling item instance
         */
        fun create(
            userId: String,
            photoUri: String,
            description: String,
            category: MaterialCategory,
            weightRange: WeightRange
        ): RecyclingItem {
            val points = weightRange.calculatePoints(category)
            
            return RecyclingItem(
                userId = userId,
                photoUri = photoUri,
                description = description.trim(),
                category = category,
                weightRange = weightRange,
                pointsEarned = points
            )
        }
    }
}
