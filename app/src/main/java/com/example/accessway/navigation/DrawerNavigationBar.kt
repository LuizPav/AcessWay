package com.example.accessway.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.accessway.ui.theme.LogoBlue
import com.example.accessway.ui.theme.TextDarkGray

@Composable
fun DrawerNavigationBar(
    navController: NavController,
    onItemClick: () -> Unit
) {

    val items = listOf(
        Screen.Home,
        Screen.Routes,
        Screen.Favorites,
        Screen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    items.forEach { screen ->
        val selected = currentRoute == screen.route

        NavigationDrawerItem(
            label = {
                Text(
                    text = screen.title,
                    fontSize = 15.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                )
            },
            selected = selected,
            icon = {
                Icon(
                    imageVector = screen.icon,
                    contentDescription = screen.title
                )
            },
            onClick = {
                if (currentRoute != screen.route) {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                onItemClick()
            },
            shape = RoundedCornerShape(14.dp),
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = LogoBlue.copy(alpha = 0.12f),
                selectedIconColor = LogoBlue,
                selectedTextColor = LogoBlue,

                unselectedContainerColor = Color.Transparent,
                unselectedIconColor = TextDarkGray.copy(alpha = 0.7f),
                unselectedTextColor = TextDarkGray
            ),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}