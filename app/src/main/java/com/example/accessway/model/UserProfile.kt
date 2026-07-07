package com.example.accessway.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val needsWheelchair: Boolean = false,
    val needsTactilePaving: Boolean = false,
    val needsAudioAlerts: Boolean = false
)