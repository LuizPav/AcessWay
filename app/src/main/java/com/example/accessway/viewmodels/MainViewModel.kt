package com.example.accessway.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var isLogged by mutableStateOf(false)
    var isRegisterActive by mutableStateOf(false)

    fun login() {
        isLogged = true
    }

    fun logout() {
        isLogged = false
    }

    fun goToRegister() {
        isRegisterActive = true
    }

    fun backFromRegister() {
        isRegisterActive = false
    }
}
