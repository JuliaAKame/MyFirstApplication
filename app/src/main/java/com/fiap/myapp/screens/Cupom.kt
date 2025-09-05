package com.fiap.myapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fiap.myapp.viewmodel.Cupom
import com.fiap.myapp.viewmodel.CuponsViewModel

@Composable
fun CuponsScreen(viewModel: CuponsViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9)) // verde claro
            .padding(16.dp)
    ) {
        Text(
            text = "Meus Cupons e Resgates",
            fontSize = 22.sp,
            color = Color(0xFF2E7D32),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(viewModel.cupons) { cupom ->
                CupomCard(cupom)
            }
        }
    }
}

@Composable
fun CupomCard(cupom: Cupom) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = cupom.titulo, fontSize = 18.sp, color = Color(0xFF2E7D32))
            Text(text = cupom.descricao, fontSize = 14.sp, color = Color(0xFF757575))
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${cupom.pontosNecessarios} pontos",
                    color = Color(0xFF4CAF50),
                    fontSize = 14.sp
                )
                Button(
                    onClick = { /* Ação de resgate */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Resgatar", color = Color.White)
                }
            }
        }
    }
}
