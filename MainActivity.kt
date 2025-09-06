package com.rm561000

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rm561000.ui.cupons.CuponsScreen
import com.rm561000.viewmodel.CuponsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o conteúdo da atividade como a tela de cupons.
        setContent {
            CuponsScreen(viewModel = CuponsViewModel())
        }
    }
}


