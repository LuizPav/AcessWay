package com.example.accessway.model

data class Favorite(
    val id: String,
    val name: String,
    val address: String,
    val accessibilityRating: Int = 5, // Rating out of 5
    val latitude: Double,
    val longitude: Double
)
