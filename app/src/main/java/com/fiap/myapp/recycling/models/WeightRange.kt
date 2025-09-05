package com.fiap.myapp.recycling.models

/**
 * Represents different weight ranges for recyclable materials.
 * Weight affects the points calculation multiplier.
 */
enum class WeightRange(
    val displayName: String,
    val multiplier: Float,
    val minGrams: Int,
    val maxGrams: Int?
) {
    VERY_LIGHT(
        displayName = "Muito Leve (até 50g)",
        multiplier = 1.0f,
        minGrams = 0,
        maxGrams = 50
    ),
    
    LIGHT(
        displayName = "Leve (51g - 200g)",
        multiplier = 1.5f,
        minGrams = 51,
        maxGrams = 200
    ),
    
    MEDIUM(
        displayName = "Médio (201g - 500g)",
        multiplier = 2.0f,
        minGrams = 201,
        maxGrams = 500
    ),
    
    HEAVY(
        displayName = "Pesado (501g - 1kg)",
        multiplier = 3.0f,
        minGrams = 501,
        maxGrams = 1000
    ),
    
    VERY_HEAVY(
        displayName = "Muito Pesado (mais de 1kg)",
        multiplier = 4.0f,
        minGrams = 1001,
        maxGrams = null
    );
    
    /**
     * Calculates points based on category base points and weight multiplier.
     * @param category The material category
     * @return Calculated points for this weight range
     */
    fun calculatePoints(category: MaterialCategory): Int {
        return (category.basePoints * multiplier).toInt()
    }
    
    companion object {
        /**
         * Suggests weight range based on material category defaults.
         * @param category The material category
         * @return Most common weight range for the category
         */
        fun getDefaultForCategory(category: MaterialCategory): WeightRange {
            return when (category) {
                MaterialCategory.PAPER -> LIGHT
                MaterialCategory.PLASTIC -> LIGHT
                MaterialCategory.GLASS -> MEDIUM
                MaterialCategory.METAL -> MEDIUM
                MaterialCategory.ORGANIC -> MEDIUM
            }
        }
    }
}
