package com.example.accessway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.accessway.screens.HomeScreen
import com.example.accessway.screens.LoginScreen
import com.example.accessway.screens.RegisterScreen
import com.example.accessway.ui.theme.AccessWayTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            AccessWayTheme {

                var isLogged by remember {
                    mutableStateOf(false)
                }
                var isRegisterActive by remember {
                    mutableStateOf(false)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    if (isLogged) {

                        HomeScreen(
                            modifier = Modifier.padding(innerPadding),
                            goBack = { isLogged = false }
                        )

                    } else if(isRegisterActive) {
                        RegisterScreen(
                            modifier = Modifier.padding(innerPadding),
                            goBack = { isRegisterActive = false },
                            submit = { isRegisterActive = false }
                        )
                    } else {

                        LoginScreen(
                            onLoginClick = {
                                isLogged = true
                            },
                            onRegisterClick = {
                                isRegisterActive = true
                            }
                        )

                    }
                }
            }
        }
    }
}