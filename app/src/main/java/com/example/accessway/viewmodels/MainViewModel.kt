package com.example.accessway.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.accessway.repository.AuthRepository

class MainViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    var isLogged by mutableStateOf(
        authRepository.getCurrentUserUid() != null
    )
        private set

    var isRegisterActive by mutableStateOf(false)
        private set

    fun login() {
        isLogged = true
    }

    fun logout() {
        authRepository.logout()
        isLogged = false
    }

    fun goToRegister() {
        isRegisterActive = true
    }

    fun backFromRegister() {
        isRegisterActive = false
    }
}
