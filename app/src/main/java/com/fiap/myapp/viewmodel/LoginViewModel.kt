package com.fiap.myapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val senha: String = "",
    val erroLogin: String = ""
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChange(novoEmail: String) {
        _uiState.value = _uiState.value.copy(email = novoEmail)
    }

    fun onSenhaChange(novaSenha: String) {
        _uiState.value = _uiState.value.copy(senha = novaSenha)
    }

    fun tentarLogin() {
        val estadoAtual = _uiState.value

        viewModelScope.launch {
            val erro = when {
                estadoAtual.email.isBlank() -> "Preencha o e-mail"
                estadoAtual.senha.isBlank() -> "Preencha a senha"
                else -> ""
            }

            _uiState.value = estadoAtual.copy(erroLogin = erro)

            if (erro.isEmpty()) {
                _uiState.value = estadoAtual.copy(erroLogin = "")
            }
        }
    }
}
