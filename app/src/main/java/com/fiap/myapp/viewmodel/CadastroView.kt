package com.fiap.myapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CadastroViewModel : ViewModel() {
    val nome = MutableLiveData("")
    val dataNascimento = MutableLiveData("")
    val email = MutableLiveData("")
    val telefone = MutableLiveData("")
    val senha = MutableLiveData("")
    val confirmarSenha = MutableLiveData("")
    val aceitarTermos = MutableLiveData(false)

    fun isFormularioValido(): Boolean {
        return !nome.value.isNullOrBlank()
                && !email.value.isNullOrBlank()
                && senha.value == confirmarSenha.value
                && aceitarTermos.value == true
    }
}
