package com.fiap.myapp.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fiap.myapp.R
import com.fiap.myapp.auth.AuthState
import com.fiap.myapp.auth.AuthViewModel
import com.fiap.myapp.ui.theme.BLUE
import com.fiap.myapp.ui.theme.GREEN
import com.fiap.myapp.ui.theme.WHITE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(
    authViewModel: AuthViewModel = viewModel(),
    onNavigateToLogin: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val minhaFonte = FontFamily(Font(R.font.righteous_regular))
    
    val authState by authViewModel.authState.collectAsState()

    var nome by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var aceitarTermos by remember { mutableStateOf(false) }
    var erroCadastro by remember { mutableStateOf("") }
    
    val isLoading = authState is AuthState.Loading
    
    LaunchedEffect(authState) {
        val currentState = authState
        when (currentState) {
            is AuthState.Error -> {
                erroCadastro = currentState.message
            }
            is AuthState.Authenticated -> {
                Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                onNavigateToLogin()
                erroCadastro = ""
            }
            is AuthState.Unauthenticated -> {
                if (erroCadastro.isNotEmpty()) {
                    erroCadastro = ""
                }
            }
        }
    }
    
    fun validateFields(): String? {
        return when {
            nome.isBlank() -> "Nome é obrigatório"
            email.isBlank() -> "Email é obrigatório"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Email inválido"
            senha.length < 6 -> "Senha deve ter pelo menos 6 caracteres"
            senha != confirmarSenha -> "Senhas não coincidem"
            !aceitarTermos -> "Aceite os termos e condições"
            else -> null
        }
    }
    
    fun handleSignUp() {
        val validationError = validateFields()
        if (validationError != null) {
            erroCadastro = validationError
            return
        }
        
        erroCadastro = ""
        authViewModel.signUp(email, senha)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(colors = listOf(WHITE, GREEN, BLUE))
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onBack,
            modifier = Modifier.padding(top = 16.dp),
            enabled = !isLoading
        ) {
            Text(text = "Voltar para Login")
        }

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = buildAnnotatedString {
                append("Cadas")
                withStyle(style = SpanStyle(color = BLUE, fontFamily = minhaFonte)) {
                    append("tro")
                }
            },
            fontSize = 32.sp,
            color = WHITE,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome completo") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        OutlinedTextField(
            value = dataNascimento,
            onValueChange = { dataNascimento = it },
            label = { Text("Data de nascimento") },
            placeholder = { Text("DD/MM/AAAA") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = !isLoading
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            enabled = !isLoading,
            isError = erroCadastro.contains("Email", ignoreCase = true)
        )

        OutlinedTextField(
            value = telefone,
            onValueChange = { telefone = it },
            label = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            enabled = !isLoading
        )

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            enabled = !isLoading,
            isError = erroCadastro.contains("Senha", ignoreCase = true)
        )

        OutlinedTextField(
            value = confirmarSenha,
            onValueChange = { confirmarSenha = it },
            label = { Text("Confirmar senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            enabled = !isLoading,
            isError = erroCadastro.contains("coincidem", ignoreCase = true)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Checkbox(
                checked = aceitarTermos,
                onCheckedChange = { aceitarTermos = it },
                enabled = !isLoading
            )
            Text(text = "Aceito os termos e condições", color = WHITE)
        }

        if (erroCadastro.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = erroCadastro,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    if (authState is AuthState.Error) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { authViewModel.clearError() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Tentar Novamente", color = WHITE)
                        }
                    }
                }
            }
        }

        Button(
            onClick = { handleSignUp() },
            modifier = Modifier.fillMaxWidth(),
            enabled = aceitarTermos && !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = GREEN)
        ) {
            if (isLoading) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = WHITE,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cadastrando...", color = WHITE)
                }
            } else {
                Text("Cadastrar", color = WHITE)
            }
        }
    }
}
