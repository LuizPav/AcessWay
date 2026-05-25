package com.example.accessway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.accessway.screens.LoginScreen
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

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    if (isLogged) {

                        HomePage(
                            modifier = Modifier.padding(innerPadding)
                        )

                    } else {

                        LoginScreen(
                            onLoginClick = {
                                isLogged = true
                            },
                            onRegisterClick = {

                            }
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun HomePage(modifier: Modifier = Modifier) {

    Column(modifier = modifier) {

        Text(
            text = "Tela Principal",
            fontSize = 24.sp
        )

        // SearchBar
        Row {

        }

        // Mapa
        Box {

        }

        // Detalhes
        Column {

            Row {

                // Logo Avaliação
                Box {

                }

                // Detalhes
                Column {

                }

                // Avaliação
                Box {

                }
            }
        }
    }
}