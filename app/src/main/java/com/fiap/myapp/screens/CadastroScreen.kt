package com.fiap.myapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiap.myapp.auth.AuthState
import com.fiap.myapp.auth.AuthViewModel
import com.fiap.myapp.screens.MinhaFonte
import com.fiap.myapp.ui.theme.BLUE
import com.fiap.myapp.ui.theme.GREEN
import com.fiap.myapp.ui.theme.WHITE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(
    authViewModel: AuthViewModel? = null,
    onNavigateToLogin: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    val authState by (authViewModel?.authState?.collectAsState() ?: remember { mutableStateOf(AuthState.Unauthenticated) })
    val isLoading = authState is AuthState.Loading
    
    // Validações
    val isEmailValid = email.contains("@") && email.contains(".")
    val isPasswordValid = password.length >= 6
    val doPasswordsMatch = password == confirmPassword && confirmPassword.isNotEmpty()
    val isFormValid = name.isNotBlank() && isEmailValid && isPasswordValid && doPasswordsMatch
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(GREEN, BLUE)
                )
            )
    ) {
        // TopBar
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(
                    onClick = onBack,
                    enabled = !isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = WHITE
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título
            Text(
                text = buildAnnotatedString {
                    append("CRIAR CONTA\n")
                    withStyle(
                        style = SpanStyle(
                            color = BLUE,
                            fontFamily = MinhaFonte
                        )
                    ) {
                        append("CLEANWORLD")
                    }
                },
                color = WHITE,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // Formulário
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
                    // Campo Nome
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome completo", color = WHITE.copy(alpha = 0.8f)) },
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
                            focusedBorderColor = if (email.isNotEmpty() && !isEmailValid) 
                                MaterialTheme.colorScheme.error else WHITE,
                            unfocusedBorderColor = WHITE.copy(alpha = 0.5f)
                        ),
                        singleLine = true,
                        enabled = !isLoading,
                        isError = email.isNotEmpty() && !isEmailValid
                    )
                    
                    if (email.isNotEmpty() && !isEmailValid) {
                        Text(
                            text = "Email deve conter @ e domínio",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, bottom = 8.dp)
                        )
                    }
                    
                    // Campo Senha
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Senha", color = WHITE.copy(alpha = 0.8f)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = WHITE,
                            unfocusedTextColor = WHITE,
                            focusedBorderColor = if (password.isNotEmpty() && !isPasswordValid) 
                                MaterialTheme.colorScheme.error else WHITE,
                            unfocusedBorderColor = WHITE.copy(alpha = 0.5f)
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        enabled = !isLoading,
                        isError = password.isNotEmpty() && !isPasswordValid
                    )
                    
                    if (password.isNotEmpty() && !isPasswordValid) {
                        Text(
                            text = "Senha deve ter pelo menos 6 caracteres",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, bottom = 8.dp)
                        )
                    }
                    
                    // Campo Confirmar Senha
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar senha", color = WHITE.copy(alpha = 0.8f)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = WHITE,
                            unfocusedTextColor = WHITE,
                            focusedBorderColor = if (confirmPassword.isNotEmpty() && !doPasswordsMatch) 
                                MaterialTheme.colorScheme.error else WHITE,
                            unfocusedBorderColor = WHITE.copy(alpha = 0.5f)
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        enabled = !isLoading,
                        isError = confirmPassword.isNotEmpty() && !doPasswordsMatch
                    )
                    
                    if (confirmPassword.isNotEmpty() && !doPasswordsMatch) {
                        Text(
                            text = "Senhas não conferem",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, bottom = 16.dp)
                        )
                    }
                    
                    // Botão Cadastrar
                    Button(
                        onClick = { 
                            authViewModel?.signUp(email.trim(), password)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BLUE
                        ),
                        enabled = !isLoading && isFormValid
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = WHITE,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(
                                text = "CRIAR CONTA",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = WHITE
                            )
                        }
                    }
                    
                    // Link para login
                    Row(
                        modifier = Modifier.padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Já tem conta? ",
                            color = WHITE.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        TextButton(
                            onClick = onNavigateToLogin,
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Fazer Login",
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
fun CadastroScreenPreview() {
    CadastroScreen()
}