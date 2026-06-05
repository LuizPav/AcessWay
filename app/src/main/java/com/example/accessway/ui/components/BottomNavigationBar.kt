package com.example.accessway.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.accessway.navigation.Screen

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {

    NavigationBar {

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(Screen.Home.route)
            },
            icon = {
                Icon(Icons.Default.Home, null)
            },
            label = {
                Text("Início")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(Screen.Routes.route)
            },
            icon = {
                Icon(Icons.Default.Place, null)
            },
            label = {
                Text("Rotas")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(Screen.Favorites.route)
            },
            icon = {
                Icon(Icons.Default.FavoriteBorder, null)
            },
            label = {
                Text("Favoritos")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(Screen.Profile.route)
            },
            icon = {
                Icon(Icons.Default.Person, null)
            },
            label = {
                Text("Perfil")
            }
        )
    }
}