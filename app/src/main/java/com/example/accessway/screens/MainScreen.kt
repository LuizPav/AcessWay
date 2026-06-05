package com.example.accessway.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.accessway.ui.components.BottomNavigationBar
import com.example.accessway.navigation.Screen
import com.example.accessway.viewmodels.MainViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier.fillMaxSize().padding(paddingValues)
        ) {

            composable(Screen.Home.route) {
                HomeScreen(
                    modifier = modifier,
                    goBack = { mainViewModel.logout() }
                )
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