package com.example.accessway.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accessway.repository.AuthRepository
import com.example.accessway.ui.state.AuthState
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // Repositório
    private val authRepository = AuthRepository()

    // Estado da UI
    var email by mutableStateOf("")
        private set
    var senha by mutableStateOf("")
        private set

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(newValue: String) { email = newValue }
    fun onSenhaChange(newValue: String) { senha = newValue }

    fun login(onSuccess: () -> Unit) {
        // Validação básica
        if (email.isBlank() || senha.isBlank()) {
            errorMessage = "Preencha todos os campos."
            return
        }

        viewModelScope.launch {
            authState = AuthState.Loading
            errorMessage = null

            val result = authRepository.login(email, senha)

            result.fold(
                onSuccess = {
                    authState = AuthState.Success
                    onSuccess()
                },
                onFailure = {
                    val msg = it.message ?: "Erro ao realizar login."
                    authState = AuthState.Error(msg)
                    errorMessage = msg
                }
            )
        }
    }
}