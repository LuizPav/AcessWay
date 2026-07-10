package com.example.accessway.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.accessway.navigation.Screen
import com.example.accessway.navigation.DrawerNavigationBar
import com.example.accessway.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.accessway.ui.theme.LogoBlue
import com.example.accessway.viewmodels.ProfileViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()
    val profileViewModel = ProfileViewModel();

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isHome = currentRoute == Screen.Home.route
    val gestureEnabled = !isHome || drawerState.isOpen


    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gestureEnabled,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .padding(16.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(0.9f)
                        .padding(start = 16.dp)
                            .clickable {
                                scope.launch { drawerState.close() }
                                mainViewModel.logout()
                            }
                        ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Sair",
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sair",
                            color = Color.Black,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .clickable {
                                if (currentRoute != Screen.Profile.route) {
                                    scope.launch { drawerState.close() }
                                    navController.navigate(Screen.Profile.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                } else {
                                    scope.launch { drawerState.close() }
                                }
                            },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "UserPhoto",
                            tint = LogoBlue,
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(profileViewModel.profileState.name,
                            color = LogoBlue,
                            fontSize = 32.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    DrawerNavigationBar(
                        navController = navController,
                        onItemClick = {
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.Routes.route) {
                    RoutesScreen(
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.Profile.route) {
                    ProfileScreen(
                        onOpenMenu = { scope.launch { drawerState.open() } },
                        onLogout = { mainViewModel.logout() }
                    )
                }
            }
        }
    }
}