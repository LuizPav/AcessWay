package com.example.accessway.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.accessway.ui.components.BottomNavigationBar
import com.example.accessway.ui.components.SearchBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    goBack: () -> Unit
) {

    Scaffold(
        containerColor = Color.White, //Forcei a Cor do Container
        bottomBar = {
            BottomNavigationBar()
        }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { goBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.Black
                    )
                }
            }

            Text(
                text = "Tela Principal",
                fontSize = 24.sp
            )

            SearchBar()

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
}