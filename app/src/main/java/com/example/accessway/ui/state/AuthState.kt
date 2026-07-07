package com.example.accessway.ui.state


sealed class AuthState {

    object Idle : AuthState()

    object Loading : AuthState()

    object Success : AuthState()

    data class Error(
        val message: String
    ) : AuthState()
}