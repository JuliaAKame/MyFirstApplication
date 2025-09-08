package com.fiap.myapp.screens

import com.fiap.myapp.R
import com.fiap.myapp.recycling.Result
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fiap.myapp.recycling.HistoryViewModel
import com.fiap.myapp.recycling.models.RecyclingItem
import com.fiap.myapp.ui.theme.BLUE
import com.fiap.myapp.ui.theme.GREEN
import com.fiap.myapp.ui.theme.WHITE
import java.text.SimpleDateFormat
import java.util.Locale

private val Righteous = FontFamily(Font(R.font.righteous_regular))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit = {},
    viewModel: HistoryViewModel = viewModel()
) {
    val historyState by viewModel.historyState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHistory("current_user_id")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Histórico de Reciclagem", fontFamily = Righteous)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GREEN),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = WHITE
                        )
                    }
                }
            )
        },
        containerColor = Brush.linearGradient(colors = listOf(WHITE, GREEN, BLUE)).let { androidx.compose.ui.graphics.Color.Unspecified }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = listOf(GREEN, BLUE)))
            .padding(padding)) {

            when (historyState) {
                is Result.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = WHITE)
                }
                is Result.Error -> {
                    Text(text = "Erro ao carregar histórico", color = WHITE, modifier = Modifier.align(Alignment.Center))
                }
                is Result.Success -> {
                    val items = (historyState as Result.Success<List<RecyclingItem>>).data
                    if (items.isEmpty()) {
                        Text(text = "Nenhum registro encontrado", color = WHITE, modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                            items(items) { item ->
                                HistoryItemCard(item)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryItemCard(item: RecyclingItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = WHITE.copy(alpha = 0.06f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = item.description, color = WHITE, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Categoria: ${item.category.displayName}", color = WHITE)
            Text(text = "Pontos: ${item.pointsEarned}", color = WHITE)
            val df = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            Text(text = "Data: ${df.format(item.createdAt)}", color = WHITE)
        }
    }
}
