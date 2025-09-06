package com.rm561000.model

// Data class para representar a estrutura de um Cupom.
data class Cupom(
    val id: Int,
    val title: String,
    val description: String,
    val pointsRequired: Int
)