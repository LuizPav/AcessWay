package com.example.accessway.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("cpf") val cpf: String,
    @SerializedName("password") val password: String
)
