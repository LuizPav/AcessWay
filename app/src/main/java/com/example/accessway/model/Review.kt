package com.example.accessway.model

data class Review(

    val id: String = "",

    val stopId: String = "",

    val userId: String = "",

    val stars: Int = 0,

    val acessibilidade: Int = 0,

    val pisoTatil: Int = 0,

    val iluminacao: Int = 0,

    val cobertura: Int = 0,

    val comment: String = "",

    val createdAt: Long = System.currentTimeMillis()
)