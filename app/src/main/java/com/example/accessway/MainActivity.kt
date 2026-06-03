package com.example.accessway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.accessway.screens.HomeScreen
import com.example.accessway.screens.LoginScreen
import com.example.accessway.screens.RegisterScreen
import com.example.accessway.ui.theme.AccessWayTheme

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accessway.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            AccessWayTheme {
                val mainViewModel: MainViewModel = viewModel()
                val isLogged = mainViewModel.isLogged
                val isRegisterActive = mainViewModel.isRegisterActive
                val launcher = rememberLauncherForActivityResult(contract =
                    ActivityResultContracts.RequestPermission(), onResult = {} )

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    if (isLogged) {

                        HomeScreen(
                            modifier = Modifier.padding(innerPadding),
                            goBack = { mainViewModel.logout() }
                        )

                    } else if(isRegisterActive) {
                        RegisterScreen(
                            modifier = Modifier.padding(innerPadding),
                            goBack = { mainViewModel.backFromRegister() },
                            submit = { mainViewModel.backFromRegister() }
                        )
                    } else {
                        LoginScreen(
                            onLoginClick = {
                                mainViewModel.login()
                            },
                            onRegisterClick = {
                                mainViewModel.goToRegister()
                            }
                        )
                    }
                }
            }
        }
    }
}