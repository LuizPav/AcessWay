package com.example.accessway.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home")
    object Routes : Screen("routes")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
}