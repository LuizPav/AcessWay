package com.example.accessway.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.accessway.ui.components.BottomNavigationBar
import com.example.accessway.navigation.Screen

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(Screen.Home.route) {
                HomeScreen(goBack = {})
            }

            composable(Screen.Routes.route) {
                RoutesScreen()
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen()
            }

            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}