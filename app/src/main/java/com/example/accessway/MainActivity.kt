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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.accessway.ui.theme.AccessWayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AccessWayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomePage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column{
//     SearchBar
        Row{

        }
//     Mapa
        Box{

        }
//     Detalhes
        Column{
            Row {
//            Logo Avaliação
                Box{

                }
//            Detalhes
                Column{

                }
//            Avaliação
                Box {

                }
            }
        }
    }
}