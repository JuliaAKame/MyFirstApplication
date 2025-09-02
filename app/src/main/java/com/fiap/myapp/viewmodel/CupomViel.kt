package com.fiap.myapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class Cupom(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val pontosNecessarios: Int
)


class CuponsViewModel : ViewModel() {

    val cupons: SnapshotStateList<Cupom> = mutableStateListOf(
        Cupom(1, "10% de desconto no Mercado Verde", "Use 200 pontos para economizar", 200),
        Cupom(2, "R$20 off em roupas sustentáveis", "Troque 500 pontos", 500),
        Cupom(3, "Ingresso para evento ecológico", "Resgate por 800 pontos", 800),
        Cupom(4, "Cupom para restaurante vegano", "Apenas 300 pontos", 300)
    )
}
