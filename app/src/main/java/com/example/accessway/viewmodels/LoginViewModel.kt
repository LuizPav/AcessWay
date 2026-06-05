package com.example.accessway.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
    var senha by mutableStateOf("")

    val isLoginEnabled: Boolean
        get() = email.isNotEmpty() && senha.isNotEmpty()

    fun onEmailChange(newValue: String) {
        email = newValue
    }

    fun onSenhaChange(newValue: String) {
        senha = newValue
    }
}
