package com.example.accessway.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accessway.model.Favorite
import com.example.accessway.ui.theme.BackgroundWhite
import com.example.accessway.ui.theme.LogoBlue
import com.example.accessway.ui.theme.LogoGreen
import com.example.accessway.ui.theme.TextDarkGray
import com.example.accessway.ui.theme.TextLightGray
import com.example.accessway.ui.theme.TextMediumGray
import com.example.accessway.viewmodels.FavoritesViewModel

@Composable
fun FavoritesScreen(
    onOpenMenu: () -> Unit = {},
    viewModel: FavoritesViewModel = viewModel(),
    onFavoriteClick: (Favorite) -> Unit = {},
    onFavoriteRemoved: (String) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.loadFavoriteStops()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .statusBarsPadding()
    ) {
        // --- CUSTOM HEADER / TOP BAR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onOpenMenu,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.2f))
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Abrir Menu",
                    tint = LogoBlue
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Meus Favoritos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = LogoBlue
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- FAVORITES LIST ---
        if (viewModel.favoriteStops.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Sem favoritos",
                        tint = Color.LightGray,
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Nenhum favorito salvo",
                        color = TextMediumGray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Favorite paradas de ônibus no mapa para visualizá-las aqui rapidamente.",
                        color = TextLightGray,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        lineHeight = 20.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Paradas Favoritas",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = LogoBlue,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(viewModel.favoriteStops, key = { "stop_${it.id}" }) { favorite ->
                    FavoriteCard(
                        favorite = favorite,
                        onDeleteClick = {
                            viewModel.removeFavorite(favorite.id)
                            onFavoriteRemoved(favorite.id)
                        },
                        onClick = { onFavoriteClick(favorite) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun FavoriteCard(
    favorite: Favorite,
    onDeleteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Badge
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(LogoBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = LogoBlue,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = favorite.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDarkGray
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = favorite.address,
                    fontSize = 13.sp,
                    color = TextMediumGray,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Rating representation
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Acessibilidade: ",
                        fontSize = 11.sp,
                        color = TextLightGray
                    )
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < favorite.accessibilityRating) LogoGreen else Color.LightGray.copy(alpha = 0.6f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Delete Button
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Red.copy(alpha = 0.08f))
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Remover Favorito",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}