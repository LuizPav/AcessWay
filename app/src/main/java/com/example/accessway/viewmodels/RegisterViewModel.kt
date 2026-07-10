package com.example.accessway.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accessway.model.UserProfile
import com.example.accessway.repository.AuthRepository
import com.example.accessway.repository.UserRepository
import com.example.accessway.ui.state.AuthState
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set


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

    fun register(onSuccess: () -> Unit) {

        errorMessage = null

        if (name.isBlank()) {
            errorMessage = "Nome não pode ser vazio."
            return
        }

        if (!email.isValidEmail()) {
            errorMessage = "Email inválido."
            return
        }

        if (password.length < 6) {
            errorMessage = "A senha deve possuir no mínimo 6 caracteres."
            return
        }

        if (password != confirmPassword) {
            errorMessage = "As senhas não conferem."
            return
        }

        viewModelScope.launch {

            authState = AuthState.Loading

            val registerResult =
                authRepository.register(email, password)

            registerResult.fold(

                onSuccess = { uid ->

                    val profile = UserProfile(
                        uid = uid,
                        name = name,
                        email = email
                    )

                    val saveResult =
                        userRepository.saveUser(profile)

                    saveResult.fold(

                        onSuccess = {

                            authState = AuthState.Success
                            onSuccess()

                        },

                        onFailure = {

                            authState =
                                AuthState.Error(
                                    it.message ?: "Erro ao salvar usuário."
                                )

                            errorMessage = it.message
                        }

                    )

                },

                onFailure = { throwable ->
                    val msg = when (throwable) {
                        is com.google.firebase.auth.FirebaseAuthUserCollisionException -> {
                            "Este e-mail já está cadastrado."
                        }
                        is com.google.firebase.auth.FirebaseAuthWeakPasswordException -> {
                            "A senha informada é muito fraca."
                        }
                        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> {
                            "O endereço de e-mail está mal formatado."
                        }
                        is com.google.firebase.FirebaseNetworkException -> {
                            "Sem conexão com a internet. Verifique sua rede."
                        }
                        else -> {
                            val message = throwable.message ?: ""
                            when {
                                message.contains("email already", ignoreCase = true) -> "Este e-mail já está cadastrado."
                                message.contains("badly formatted", ignoreCase = true) -> "O endereço de e-mail está mal formatado."
                                else -> "Erro ao cadastrar. Tente novamente."
                            }
                        }
                    }
                    authState = AuthState.Error(msg)
                    errorMessage = msg
                }

            )

        }

    }
}
