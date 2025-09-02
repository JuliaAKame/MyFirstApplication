package com.fiap.myapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.fiap.myapp.R
import com.fiap.myapp.ui.theme.BLUE
import com.fiap.myapp.ui.theme.GREEN
import com.fiap.myapp.ui.theme.WHITE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var erroLogin by remember { mutableStateOf("") }
    val minhaFonte = FontFamily(Font(R.font.righteous_regular))



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(WHITE, GREEN, BLUE)
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp)
        )


        Text(
            text = buildAnnotatedString {
                append("CLEAN")
                withStyle(style = SpanStyle(color = BLUE, fontFamily = minhaFonte)) {
                    append("WORLD")
                }
            },
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = WHITE,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Blue,
            )
        )

        OutlinedTextField(
            shape = RoundedCornerShape(9.dp),
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Blue,
            )
        )

        if (erroLogin.isNotEmpty()) {
            Text(
                text = erroLogin,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                erroLogin = when {
                    email.isBlank() -> "Preencha o e-mail"
                    senha.isBlank() -> "Preencha a senha"
                    else -> ""
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BLUE)
        ) {
            Text(
                text = "Entrar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = WHITE
            )
        }

        Button(
            onClick = {
                navController.navigate("cadastro")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GREEN)
        ) {
            Text(
                text = "Cadastrar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = WHITE
            )
        }
    }
}


