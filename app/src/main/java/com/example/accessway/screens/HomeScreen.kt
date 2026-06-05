package com.example.accessway.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accessway.ui.components.MapBase
import com.example.accessway.ui.components.SearchBar
import com.example.accessway.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    onOpenMenu: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        MapBase(
            viewModel = viewModel
        )

        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 55.dp, start = 16.dp, end = 16.dp),
            onMenuClick = onOpenMenu,
        )
    }
}