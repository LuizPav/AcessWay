package com.example.accessway.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var errorMessage by mutableStateOf<String?>(null)

    private fun String.isValidEmail(): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
        return this.matches(emailRegex)
    }

    val emailError: Boolean
        get() = email.isNotEmpty() && !email.isValidEmail()

    val passwordError: Boolean
        get() = password.isNotEmpty() && password.length < 6

    val passwordMismatchError: Boolean
        get() = confirmPassword.isNotEmpty() && password != confirmPassword

    fun onNameChange(newValue: String) {
        name = newValue
    }

    fun onEmailChange(newValue: String) {
        email = newValue
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
    }

    fun onConfirmPasswordChange(newValue: String) {
        confirmPassword = newValue
    }

    fun handleRegister(onSuccess: () -> Unit) {
        errorMessage = null

        if (name.isBlank()) {
            errorMessage = "Erro ao registrar conta, nome não pode ser vazio."
            return
        }
        if (password.length < 6) {
            errorMessage = "A senha deve ter pelo menos 6 caracteres."
            return
        }
        if (password != confirmPassword) {
            errorMessage = "Erro ao registrar conta, Senhas diferentes."
            return
        }
        if (!email.isValidEmail()) {
            errorMessage = "Erro ao registrar conta, Email inválido."
            return
        }

        onSuccess()
    }
}
