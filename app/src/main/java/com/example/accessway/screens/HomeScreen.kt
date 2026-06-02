package com.example.accessway.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accessway.ui.components.MapBase
import com.example.accessway.ui.components.SearchBar
import com.example.accessway.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    goBack: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {

        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            MapBase(viewModel = viewModel)

            SearchBar(
                modifier = Modifier.align(Alignment.TopCenter)
                    .padding(top = 55.dp, start = 16.dp, end = 16.dp)
            )

            IconButton(
                modifier = Modifier.padding(start = 4.dp, top = 6.dp)
                    .background(Color.White, shape = RoundedCornerShape(24.dp))
                    .border(1.dp, Color.Black,RoundedCornerShape(24.dp)),
                onClick = { goBack() },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.Black
                )
            }
        }
    }
}