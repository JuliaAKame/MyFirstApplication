package com.fiap.myapp.recycling.models

/**
 * Represents the different categories of recyclable materials.
 * Each category has associated point values based on environmental impact.
 */
enum class MaterialCategory(
    val displayName: String,
    val basePoints: Int,
    val color: Long,
    val keywords: List<String>
) {
    PLASTIC(
        displayName = "Plástico",
        basePoints = 10,
        color = 0xFF4CAF50, // Green
        keywords = listOf(
            "garrafa", "pet", "plástico", "sacola", "embalagem", 
            "tampa", "pote", "recipiente", "descartável"
        )
    ),
    
    GLASS(
        displayName = "Vidro",
        basePoints = 15,
        color = 0xFF2196F3, // Blue
        keywords = listOf(
            "vidro", "garrafa", "pote", "frasco", "jar", 
            "taça", "copo", "recipiente"
        )
    ),
    
    PAPER(
        displayName = "Papel",
        basePoints = 5,
        color = 0xFFFFC107, // Amber
        keywords = listOf(
            "papel", "revista", "jornal", "cardboard", "papelão",
            "caixa", "envelope", "folha", "documento"
        )
    ),
    
    METAL(
        displayName = "Metal",
        basePoints = 20,
        color = 0xFF9E9E9E, // Grey
        keywords = listOf(
            "metal", "lata", "alumínio", "ferro", "aço",
            "tampa", "parafuso", "fio", "cabo"
        )
    ),
    
    ORGANIC(
        displayName = "Orgânico",
        basePoints = 8,
        color = 0xFF795548, // Brown
        keywords = listOf(
            "orgânico", "fruta", "verdura", "casca", "folha",
            "resto", "comida", "alimento", "natural"
        )
    );
    
    /**
     * Validates if the description contains relevant keywords for this category.
     * @param description User-provided description of the recyclable item
     * @return true if description is relevant to this category
     */
    fun validateDescription(description: String): Boolean {
        val lowercaseDescription = description.lowercase()
        return keywords.any { keyword -> 
            lowercaseDescription.contains(keyword.lowercase())
        }
    }
    
    companion object {
        /**
         * Suggests the most appropriate category based on description keywords.
         * @param description User-provided description
         * @return Suggested category or null if no match found
         */
        fun suggestCategory(description: String): MaterialCategory? {
            return values().find { category ->
                category.validateDescription(description)
            }
        }
    }
}
