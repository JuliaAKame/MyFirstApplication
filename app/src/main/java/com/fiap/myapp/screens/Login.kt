package com.fiap.myapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiap.myapp.R
import com.fiap.myapp.auth.AuthState
import com.fiap.myapp.auth.AuthViewModel
import com.fiap.myapp.ui.theme.BLUE
import com.fiap.myapp.ui.theme.GREEN
import com.fiap.myapp.ui.theme.WHITE

/**
 * Login screen with full AuthViewModel integration.
 * Supports authentication states, loading, and error handling.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    authViewModel: AuthViewModel? = null,
    onNavigateToCadastro: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var erroLogin by remember { mutableStateOf("") }
    val minhaFonte = FontFamily(Font(R.font.righteous_regular))
    
    // AuthState management - handles authentication flow
    val authState by (authViewModel?.authState?.collectAsState() ?: remember { mutableStateOf(AuthState.Unauthenticated) })
    val isLoading = authState is AuthState.Loading
    
    // Handle auth errors automatically
    LaunchedEffect(authState) {
        val currentState = authState
        when (currentState) {
            is AuthState.Error -> {
                erroLogin = currentState.message
            }
            is AuthState.Authenticated -> {
                // Navigation handled by NavGraph
                erroLogin = ""
            }
            is AuthState.Unauthenticated -> {
                // Clear errors when returning to unauthenticated state
                if (erroLogin.isNotEmpty()) {
                    erroLogin = ""
                }
            }
            is AuthState.Loading -> {
                // Keep current error state during loading
            }
        }
    }

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

        // Email Field with improved colors
        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                // Clear local error when user starts typing
                if (erroLogin.isNotEmpty() && authViewModel == null) {
                    erroLogin = ""
                }
            },
            label = { Text("E-mail") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Blue,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            enabled = !isLoading
        )

        // Password Field with improved colors
        OutlinedTextField(
            shape = RoundedCornerShape(9.dp),
            value = senha,
            onValueChange = { 
                senha = it
                // Clear local error when user starts typing
                if (erroLogin.isNotEmpty() && authViewModel == null) {
                    erroLogin = ""
                }
            },
            label = { Text("Senha") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Blue,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading
        )

        // Error Message Display
        if (erroLogin.isNotEmpty()) {
            Text(
                text = erroLogin,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button with AuthViewModel integration
        Button(
            onClick = {
                // Input validation
                erroLogin = when {
                    email.isBlank() -> "Preencha o e-mail"
                    senha.isBlank() -> "Preencha a senha"
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "E-mail inválido"
                    else -> ""
                }
                
                // If validation passes and AuthViewModel is available, attempt authentication
                if (erroLogin.isEmpty() && authViewModel != null) {
                    authViewModel.signIn(email.trim(), senha)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BLUE),
            enabled = !isLoading && email.isNotBlank() && senha.isNotBlank(),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        color = WHITE,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Entrando...",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = WHITE
                    )
                }
            } else {
                Text(
                    text = "Entrar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = WHITE
                )
            }
        }

        // Register Button
        Button(
            onClick = onNavigateToCadastro,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GREEN),
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Cadastrar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = WHITE
            )
        }
        
        // Clear error button when there's an auth error (allows retry)
        if (authState is AuthState.Error && authViewModel != null) {
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = { 
                    authViewModel.clearError()
                    erroLogin = ""
                },
                enabled = !isLoading
            ) {
                Text(
                    text = "Tentar Novamente",
                    color = WHITE.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }
    }
}


