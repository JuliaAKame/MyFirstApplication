package com.fiap.myapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiap.myapp.R
import com.fiap.myapp.auth.AuthState
import com.fiap.myapp.auth.AuthViewModel
import com.fiap.myapp.ui.theme.BLUE
import com.fiap.myapp.ui.theme.GREEN
import com.fiap.myapp.ui.theme.WHITE

val MinhaFonte = FontFamily(
    Font(R.font.righteous_regular)
)

@Composable
fun Login(
    authViewModel: AuthViewModel? = null,
    onNavigateToCadastro: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showLoginForm by remember { mutableStateOf(false) }
    
    val authState by (authViewModel?.authState?.collectAsState() ?: remember { mutableStateOf(AuthState.Unauthenticated) })
    val isLoading = authState is AuthState.Loading
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(GREEN, BLUE)
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 20.dp)
        )
        
        // Título
        Text(
            text = buildAnnotatedString {
                append("CLEAN")
                withStyle(
                    style = SpanStyle(
                        color = BLUE,
                        fontFamily = MinhaFonte
                    )
                ) {
                    append("WORLD")
                }
            },
            color = WHITE,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (!showLoginForm) {
            // Tela inicial com descrição
            Text(
                text = "Cuidar do meio ambiente é mais do que uma responsabilidade, é uma forma de causar impacto positivo e transformar o futuro",
                color = WHITE,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 40.dp)
            )
            
            // Botões iniciais
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { showLoginForm = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(width = 1.dp, color = WHITE)
                ) {
                    Text(
                        text = "Fazer Login",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = WHITE
                    )
                }
                
                Button(
                    onClick = onNavigateToCadastro,
                    modifier = Modifier
                        .weight(1f)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(50)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BLUE
                    )
                ) {
                    Text(
                        text = "Cadastre-se",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = WHITE
                    )
                }
            }
        } else {
            // Formulário de login
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = WHITE.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Fazer Login",
                        color = WHITE,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    
                    // Campo Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = WHITE.copy(alpha = 0.8f)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = WHITE,
                            unfocusedTextColor = WHITE,
                            focusedBorderColor = WHITE,
                            unfocusedBorderColor = WHITE.copy(alpha = 0.5f)
                        ),
                        singleLine = true,
                        enabled = !isLoading
                    )
                    
                    // Campo Senha
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Senha", color = WHITE.copy(alpha = 0.8f)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = WHITE,
                            unfocusedTextColor = WHITE,
                            focusedBorderColor = WHITE,
                            unfocusedBorderColor = WHITE.copy(alpha = 0.5f)
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        enabled = !isLoading
                    )
                    
                    // Botão Login
                    Button(
                        onClick = { 
                            authViewModel?.login(email.trim(), password)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BLUE
                        ),
                        enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = WHITE,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(
                                text = "ENTRAR",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = WHITE
                            )
                        }
                    }
                    
                    // Botão Voltar
                    TextButton(
                        onClick = { 
                            showLoginForm = false
                            email = ""
                            password = ""
                            authViewModel?.clearError()
                        },
                        enabled = !isLoading
                    ) {
                        Text(
                            text = "Voltar",
                            color = WHITE.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                    
                    // Link para cadastro
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Não tem conta? ",
                            color = WHITE.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        TextButton(
                            onClick = onNavigateToCadastro,
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Cadastre-se",
                                color = WHITE,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    Login()
}