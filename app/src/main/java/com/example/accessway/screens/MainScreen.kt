package com.example.accessway.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.accessway.viewmodels.HomeViewModel
import com.example.accessway.model.Stop
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.accessway.ui.theme.LogoBlue
import com.example.accessway.viewmodels.ProfileViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import com.example.accessway.ui.theme.TextLightGray

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isHome = currentRoute == Screen.Home.route
    val gestureEnabled = !isHome || drawerState.isOpen

    LaunchedEffect(mainViewModel.isLogged) {
        if (mainViewModel.isLogged) {
            profileViewModel.loadProfile()
            homeViewModel.loadFavoriteStops()
        }
    }

    LaunchedEffect(drawerState.isOpen) {
        if (drawerState.isOpen) {
            profileViewModel.loadProfile()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gestureEnabled,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFFF8FAFC),
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // --- 1. MODERN GRADIENT HEADER ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(LogoBlue, Color(0xFF1E3A8A))
                                )
                            )
                            .statusBarsPadding()
                            .padding(24.dp)
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Profile Avatar Circle
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f))
                                        .border(2.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AccountCircle,
                                        contentDescription = "Foto de perfil",
                                        tint = Color.White,
                                        modifier = Modifier.size(52.dp)
                                    )
                                }

                                // Close Button
                                IconButton(
                                    onClick = { scope.launch { drawerState.close() } },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.15f))
                                        .size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Fechar Menu",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = profileViewModel.profileState.name.ifEmpty { "Usuário AccessWay" },
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = profileViewModel.profileState.email.ifEmpty { "acessibilidade@accessway.com" },
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    if (currentRoute != Screen.Profile.route) {
                                        navController.navigate(Screen.Profile.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Ver Perfil",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "MENU PRINCIPAL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLightGray,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )

                    // --- 2. DRAWER NAVIGATION ITEMS ---
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                    ) {
                        DrawerNavigationBar(
                            navController = navController,
                            onItemClick = { scope.launch { drawerState.close() } }
                        )
                    }

                    // --- 3. MODERN LOGOUT FOOTER ---
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f))
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch { drawerState.close() }
                                mainViewModel.logout()
                            }
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.Red.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Sair",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Sair da Conta",
                                color = Color.Red,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Encerrar sessão no aplicativo",
                                color = TextLightGray,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Text(
                        text = "AccessWay • Mobilidade Cidadã",
                        color = TextLightGray,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp)
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
                        viewModel = homeViewModel,
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
                        onOpenMenu = { scope.launch { drawerState.open() } },
                        onFavoriteClick = { favorite ->
                            val stop = Stop(
                                id = favorite.id,
                                name = favorite.name,
                                address = favorite.address,
                                avaliation = favorite.accessibilityRating.toFloat(),
                                location = LatLng(favorite.latitude, favorite.longitude),
                                isBusStop = true,
                                ratingAcessibilidade = favorite.accessibilityRating
                            )
                            homeViewModel.selectedStop = stop
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) {
                                    inclusive = false
                                }
                            }
                        },
                        onFavoriteRemoved = { id ->
                            homeViewModel.favoriteStops.removeAll { it.id == id || it.name == id }
                            homeViewModel.loadFavoriteStops()
                        }
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