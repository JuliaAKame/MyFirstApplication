package com.fiap.myapp.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    val focusManager = LocalFocusManager.current
    
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
    
    fun applyDateMask(input: String): String {
        val cleanInput = input.filter { it.isDigit() }
        return when {
            cleanInput.length <= 2 -> cleanInput
            cleanInput.length <= 4 -> "${cleanInput.substring(0, 2)}/${cleanInput.substring(2)}"
            cleanInput.length <= 8 -> "${cleanInput.substring(0, 2)}/${cleanInput.substring(2, 4)}/${cleanInput.substring(4)}"
            else -> "${cleanInput.substring(0, 2)}/${cleanInput.substring(2, 4)}/${cleanInput.substring(4, 8)}"
        }
    }
    
    fun applyPhoneMask(input: String): String {
        val cleanInput = input.filter { it.isDigit() }
        return when {
            cleanInput.length <= 2 -> cleanInput
            cleanInput.length <= 7 -> "(${cleanInput.substring(0, 2)}) ${cleanInput.substring(2)}"
            cleanInput.length <= 11 -> "(${cleanInput.substring(0, 2)}) ${cleanInput.substring(2, 7)}-${cleanInput.substring(7)}"
            else -> "(${cleanInput.substring(0, 2)}) ${cleanInput.substring(2, 7)}-${cleanInput.substring(7, 11)}"
        }
    }
    
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
            is AuthState.Loading -> {
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

    @Composable
    fun StyledTextField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        placeholder: String = "",
        leadingIcon: ImageVector,
        keyboardType: KeyboardType = KeyboardType.Text,
        imeAction: ImeAction = ImeAction.Next,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        isError: Boolean = false,
        enabled: Boolean = true,
        onNext: () -> Unit = { focusManager.moveFocus(FocusDirection.Down) }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = label,
                    tint = if (isError) MaterialTheme.colorScheme.error else BLUE
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = { onNext() },
                onDone = { focusManager.clearFocus() }
            ),
            visualTransformation = visualTransformation,
            isError = isError,
            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BLUE,
                unfocusedBorderColor = WHITE.copy(alpha = 0.7f),
                focusedLabelColor = BLUE,
                unfocusedLabelColor = WHITE.copy(alpha = 0.8f),
                cursorColor = BLUE
            )
        )
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = onBack,
                enabled = !isLoading,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = BLUE.copy(alpha = 0.8f),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar para Login",
                    tint = WHITE,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
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
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StyledTextField(
                value = nome,
                onValueChange = { nome = it },
                label = "Nome completo",
                placeholder = "Digite seu nome completo",
                leadingIcon = Icons.Filled.Person,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                enabled = !isLoading,
                isError = erroCadastro.contains("Nome", ignoreCase = true)
            )

            StyledTextField(
                value = dataNascimento,
                onValueChange = { 
                    if (it.length <= 10) {
                        dataNascimento = applyDateMask(it)
                    }
                },
                label = "Data de nascimento",
                placeholder = "DD/MM/AAAA",
                leadingIcon = Icons.Filled.DateRange,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                enabled = !isLoading
            )

            StyledTextField(
                value = email,
                onValueChange = { email = it },
                label = "E-mail",
                placeholder = "seu@email.com",
                leadingIcon = Icons.Filled.Email,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                enabled = !isLoading,
                isError = erroCadastro.contains("Email", ignoreCase = true)
            )

            StyledTextField(
                value = telefone,
                onValueChange = { 
                    if (it.length <= 15) {
                        telefone = applyPhoneMask(it)
                    }
                },
                label = "Telefone",
                placeholder = "(11) 99999-9999",
                leadingIcon = Icons.Filled.Phone,
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                enabled = !isLoading
            )

            StyledTextField(
                value = senha,
                onValueChange = { senha = it },
                label = "Senha",
                placeholder = "Mínimo 6 caracteres",
                leadingIcon = Icons.Filled.Lock,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                visualTransformation = PasswordVisualTransformation(),
                enabled = !isLoading,
                isError = erroCadastro.contains("Senha", ignoreCase = true)
            )

            StyledTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                label = "Confirmar senha",
                placeholder = "Repita sua senha",
                leadingIcon = Icons.Filled.Lock,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                visualTransformation = PasswordVisualTransformation(),
                enabled = !isLoading,
                isError = erroCadastro.contains("coincidem", ignoreCase = true),
                onNext = { focusManager.clearFocus() }
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = aceitarTermos,
                    onCheckedChange = { aceitarTermos = it },
                    enabled = !isLoading,
                    colors = CheckboxDefaults.colors(
                        checkedColor = GREEN,
                        uncheckedColor = WHITE.copy(alpha = 0.7f),
                        checkmarkColor = WHITE
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Aceito os termos e condições de uso",
                    color = WHITE,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = aceitarTermos && !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = GREEN,
                disabledContainerColor = GREEN.copy(alpha = 0.6f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = WHITE,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Cadastrando...",
                        color = WHITE,
                        fontSize = 16.sp
                    )
                }
            } else {
                Text(
                    "Cadastrar",
                    color = WHITE,
                    fontSize = 16.sp
                )
            }
        }
    }
}
