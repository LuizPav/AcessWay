package com.example.accessway.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.AltRoute
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accessway.model.Route
import com.example.accessway.ui.theme.BackgroundWhite
import com.example.accessway.ui.theme.LogoBlue
import com.example.accessway.ui.theme.LogoGreen
import com.example.accessway.ui.theme.TextDarkGray
import com.example.accessway.ui.theme.TextLightGray
import com.example.accessway.ui.theme.TextMediumGray
import com.example.accessway.viewmodels.RoutesViewModel
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoutesScreen(
    onOpenMenu: () -> Unit = {},
    viewModel: RoutesViewModel = viewModel()
) {
    val searchQuery = viewModel.searchQuery
    val selectedProfile = viewModel.selectedProfile
    val routes = viewModel.filteredRoutes

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
                text = "Rotas Acessíveis",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = LogoBlue
            )
        }

        // --- PROFILE SELECTOR (Walking vs Wheelchair) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Wheelchair Profile Button
            val isWheelchairSelected = selectedProfile == "wheelchair"
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isWheelchairSelected) LogoBlue else Color.LightGray.copy(alpha = 0.15f))
                    .border(
                        width = 1.dp,
                        color = if (isWheelchairSelected) LogoBlue else Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { viewModel.onProfileChange("wheelchair") }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Accessible,
                    contentDescription = "Perfil Cadeirante",
                    tint = if (isWheelchairSelected) Color.White else TextMediumGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cadeirante",
                    color = if (isWheelchairSelected) Color.White else TextMediumGray,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            // Pedestrian Profile Button
            val isWalkingSelected = selectedProfile == "foot-walking"
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isWalkingSelected) LogoBlue else Color.LightGray.copy(alpha = 0.15f))
                    .border(
                        width = 1.dp,
                        color = if (isWalkingSelected) LogoBlue else Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { viewModel.onProfileChange("foot-walking") }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsWalk,
                    contentDescription = "Perfil Pedestre",
                    tint = if (isWalkingSelected) Color.White else TextMediumGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pedestre",
                    color = if (isWalkingSelected) Color.White else TextMediumGray,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        // --- SEARCH BAR ---
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Buscar por origem ou destino...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = TextLightGray
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LogoBlue,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = LogoBlue
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- ROUTES LIST ---
        if (routes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AltRoute,
                        contentDescription = "Nenhuma rota",
                        tint = TextLightGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Nenhuma rota acessível encontrada",
                        color = TextMediumGray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Tente outra busca ou alterne o perfil.",
                        color = TextLightGray,
                        fontSize = 14.sp
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
                items(routes, key = { it.id }) { route ->
                    RouteCard(route = route)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RouteCard(route: Route) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title & Profile Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = route.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDarkGray,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Profile Label
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (route.profile == "wheelchair") LogoGreen.copy(alpha = 0.15f)
                            else LogoBlue.copy(alpha = 0.12f)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (route.profile == "wheelchair") "Cadeirante" else "Pedestre",
                        color = if (route.profile == "wheelchair") LogoGreen else LogoBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Origin / Destination Info
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(LogoBlue)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Origem: ${route.origin}",
                        fontSize = 13.sp,
                        color = TextMediumGray
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(LogoGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Destino: ${route.destination}",
                        fontSize = 13.sp,
                        color = TextMediumGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Distance & Duration Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column {
                    Text(
                        text = "Distância",
                        fontSize = 11.sp,
                        color = TextLightGray
                    )
                    val distanceText = if (route.distanceMeters >= 1000.0) {
                        String.format(Locale.getDefault(), "%.1f km", route.distanceMeters / 1000.0)
                    } else {
                        "${route.distanceMeters.toInt()} m"
                    }
                    Text(
                        text = distanceText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDarkGray
                    )
                }

                Column {
                    Text(
                        text = "Tempo estimado",
                        fontSize = 11.sp,
                        color = TextLightGray
                    )
                    val minutes = (route.durationSeconds / 60).toInt()
                    val durationText = if (minutes >= 60) {
                        val hours = minutes / 60
                        val remainingMinutes = minutes % 60
                        "${hours}h ${remainingMinutes}min"
                    } else {
                        "${minutes} min"
                    }
                    Text(
                        text = durationText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDarkGray
                    )
                }
            }

            // OpenRouteService Accessibility attributes
            if (route.surfaceType != null || route.maxIncline != null || route.smoothness != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Atributos OpenRouteService:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = LogoBlue,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    route.surfaceType?.let {
                        val surfacePt = translateSurface(it)
                        AttributeChip(label = "Superfície: $surfacePt", icon = null)
                    }
                    route.maxIncline?.let {
                        AttributeChip(
                            label = "Inclinação máx: $it%",
                            icon = Icons.Default.TrendingUp,
                            tint = if (it > 5) Color(0xFFD32F2F) else LogoGreen
                        )
                    }
                    route.smoothness?.let {
                        val smoothnessPt = translateSmoothness(it)
                        AttributeChip(label = "Suavidade: $smoothnessPt", icon = null)
                    }
                }
            }
        }
    }
}

@Composable
fun AttributeChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    tint: Color = LogoBlue
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextMediumGray
        )
    }
}

private fun translateSurface(surface: String): String {
    return when (surface.lowercase()) {
        "asphalt" -> "Asfalto"
        "concrete" -> "Concreto"
        "paving_stones" -> "Paralelepípedo suave"
        "cobblestone" -> "Paralelepípedo irregular"
        "unpaved" -> "Não pavimentado"
        else -> surface.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}

private fun translateSmoothness(smoothness: String): String {
    return when (smoothness.lowercase()) {
        "excellent" -> "Excelente"
        "good" -> "Boa"
        "intermediate" -> "Média"
        "bad" -> "Ruim"
        "very_bad" -> "Muito ruim"
        else -> smoothness.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}