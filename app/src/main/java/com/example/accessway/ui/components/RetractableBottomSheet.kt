package com.example.accessway.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.BorderOuter
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accessway.model.Stop
import com.example.accessway.ui.theme.LogoBlue
import com.example.accessway.ui.theme.LogoGreen
import com.example.accessway.ui.theme.TextDarkGray
import com.example.accessway.ui.theme.TextLightGray
import com.example.accessway.ui.theme.TextMediumGray
import com.example.accessway.viewmodels.HomeViewModel
import kotlin.math.roundToInt

@Composable
fun RetractableBottomSheet(
    visible: Boolean,
    stop: Stop?,
    onDismiss: () -> Unit,
    viewModel: HomeViewModel
) {
    var offsetY by remember { mutableFloatStateOf(0f) }
    var isEditing by remember { mutableStateOf(false) }

    // Reset when selected stop changes
    LaunchedEffect(stop, visible) {
        offsetY = 0f
        isEditing = false
    }

    AnimatedVisibility(
        visible = visible && stop != null,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 350)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 250)
        )
    ) {
        if (stop != null) {
            val userEval = viewModel.userEvaluations[stop.name]

            // Temporary form states
            var tempAcessibilidade by remember(stop, isEditing) { mutableStateOf(userEval?.ratingAcessibilidade ?: stop.ratingAcessibilidade) }
            var tempPisoTatil by remember(stop, isEditing) { mutableStateOf(userEval?.ratingPisoTatil ?: stop.ratingPisoTatil) }
            var tempIluminacao by remember(stop, isEditing) { mutableStateOf(userEval?.ratingIluminacao ?: stop.ratingIluminacao) }
            var tempCobertura by remember(stop, isEditing) { mutableStateOf(userEval?.ratingCobertura ?: stop.ratingCobertura) }
            var tempStars by remember(stop, isEditing) { mutableStateOf(userEval?.userStars ?: stop.avaliation.roundToInt().coerceIn(1, 5)) }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.58f) // 58% of screen height
                        .offset { IntOffset(0, offsetY.roundToInt().coerceAtLeast(0)) }
                        .shadow(16.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(
                            Color.White,
                            RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                        )
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onDragEnd = {
                                    if (offsetY > 180f) {
                                        onDismiss()
                                    } else {
                                        offsetY = 0f
                                    }
                                },
                                onVerticalDrag = { change, dragAmount ->
                                    change.consume()
                                    offsetY += dragAmount
                                }
                            )
                        }
                ) {
                    // --- DRAG HANDLE ---
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 12.dp, bottom = 8.dp)
                            .size(width = 44.dp, height = 5.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray.copy(alpha = 0.8f))
                    )

                    // --- FIXED HEADER ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stop.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDarkGray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = stop.address,
                                fontSize = 12.sp,
                                color = TextMediumGray
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.LightGray.copy(alpha = 0.2f))
                                .size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fechar",
                                tint = TextMediumGray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f))
                    )

                    // --- SCROLLABLE BODY ---
                    val bodyScrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(bodyScrollState)
                    ) {
                        if (!isEditing) {
                            // --- DETAIL MODE ---

                            // 1. Condições da parada Grid
                            Text(
                                text = "Condições da parada",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = LogoBlue,
                                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 8.dp)
                            )

                            // 2x2 Grid of features
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    ConditionCard(
                                        title = "Acessível",
                                        statusText = when (stop.ratingAcessibilidade) {
                                            3 -> "Ótimo estado"
                                            2 -> "Parcial"
                                            else -> "Ruim"
                                        },
                                        statusValue = stop.ratingAcessibilidade,
                                        icon = { color -> Icon(Icons.Default.Accessible, null, tint = color) },
                                        modifier = Modifier.weight(1f)
                                    )
                                    ConditionCard(
                                        title = "Cobertura",
                                        statusText = when (stop.ratingCobertura) {
                                            3 -> "Disponível"
                                            2 -> "Parcial"
                                            else -> "Ausente"
                                        },
                                        statusValue = stop.ratingCobertura,
                                        icon = { color -> Icon(Icons.Default.Home, null, tint = color) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    ConditionCard(
                                        title = "Piso tátil",
                                        statusText = when (stop.ratingPisoTatil) {
                                            3 -> "Bom estado"
                                            2 -> "Parcial"
                                            else -> "Ausente"
                                        },
                                        statusValue = stop.ratingPisoTatil,
                                        icon = { color -> Icon(Icons.Default.BorderOuter, null, tint = color) },
                                        modifier = Modifier.weight(1f)
                                    )
                                    ConditionCard(
                                        title = "Iluminação",
                                        statusText = when (stop.ratingIluminacao) {
                                            3 -> "Bom estado"
                                            2 -> "Parcial"
                                            else -> "Ausente"
                                        },
                                        statusValue = stop.ratingIluminacao,
                                        icon = { color -> Icon(Icons.Default.Lightbulb, null, tint = color) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color.LightGray.copy(alpha = 0.3f))
                            )

                            // 2. Avaliações Histogram section
                            Text(
                                text = "Avaliações",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = LogoBlue,
                                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 4.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Left column: Average
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(0.4f)
                                ) {
                                    Text(
                                        text = String.format("%.1f", stop.avaliation).replace(".", ","),
                                        fontSize = 42.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDarkGray
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        repeat(5) { index ->
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = if (index < stop.avaliation.roundToInt()) Color(0xFFFFB300) else Color.LightGray,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${stop.reviewCount} avaliações",
                                        fontSize = 11.sp,
                                        color = TextLightGray
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                // Right column: Distribution bars
                                Column(
                                    modifier = Modifier.weight(0.6f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    val maxCount = stop.ratingDistribution.maxOrNull()?.coerceAtLeast(1) ?: 1
                                    for (star in 5 downTo 1) {
                                        val count = stop.ratingDistribution.getOrNull(star - 1) ?: 0
                                        val fraction = count.toFloat() / maxCount
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "$star",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextMediumGray,
                                                modifier = Modifier.width(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(6.dp)
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(Color.LightGray.copy(alpha = 0.3f))
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth(fraction)
                                                        .fillMaxHeight()
                                                        .background(LogoGreen)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Action button: Avaliar esta parada
                            Button(
                                onClick = { isEditing = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = LogoBlue)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (userEval != null) "Editar minha avaliação" else "Avaliar esta parada",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                            }

                        } else {
                            // --- EVALUATION FORM MODE ---
                            Text(
                                text = "Sua Avaliação Geral",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = LogoBlue,
                                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 14.dp)
                            )

                            // Stars Selector
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                repeat(5) { index ->
                                    val currentStar = index + 1
                                    IconButton(
                                        onClick = { tempStars = currentStar },
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = if (index < tempStars) Color(0xFFFFB300) else Color.LightGray,
                                            modifier = Modifier.size(36.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color.LightGray.copy(alpha = 0.3f))
                            )

                            Text(
                                text = "Condições Específicas",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = LogoBlue,
                                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 4.dp)
                            )

                            // 4 Rating selector lines
                            FeatureRatingSelector(
                                title = "Acessibilidade",
                                selectedValue = tempAcessibilidade,
                                onValueSelected = { tempAcessibilidade = it }
                            )
                            FeatureRatingSelector(
                                title = "Cobertura",
                                selectedValue = tempCobertura,
                                onValueSelected = { tempCobertura = it }
                            )
                            FeatureRatingSelector(
                                title = "Piso tátil",
                                selectedValue = tempPisoTatil,
                                onValueSelected = { tempPisoTatil = it }
                            )
                            FeatureRatingSelector(
                                title = "Iluminação",
                                selectedValue = tempIluminacao,
                                onValueSelected = { tempIluminacao = it }
                            )

                            // Save & Cancel buttons
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { isEditing = false },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(text = "Cancelar", fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = {
                                        viewModel.submitEvaluation(
                                            stopId = stop.id,
                                            acessibilidade = tempAcessibilidade,
                                            pisoTatil = tempPisoTatil,
                                            iluminacao = tempIluminacao,
                                            cobertura = tempCobertura,
                                            userStars = tempStars
                                        )
                                        isEditing = false
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = LogoBlue)
                                ) {
                                    Text(text = "Salvar", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConditionCard(
    title: String,
    statusText: String,
    statusValue: Int, // 1 = Ruim, 2 = Parcial, 3 = Bom
    icon: @Composable (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, contentColor) = when (statusValue) {
        3 -> Color(0xFFE8F5E9) to Color(0xFF2E7D32) // Verde suave
        2 -> Color(0xFFFFF3E0) to Color(0xFFEF6C00) // Laranja suave
        else -> Color(0xFFFFEBEE) to Color(0xFFC62828) // Vermelho suave
    }

    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(contentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                icon(contentColor)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = statusText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun FeatureRatingSelector(
    title: String,
    selectedValue: Int,
    onValueSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextDarkGray
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val options = listOf(
                1 to ("Ruim/Ausente" to Color(0xFFFFEBEE) to Color(0xFFC62828)),
                2 to ("Parcial" to Color(0xFFFFF3E0) to Color(0xFFEF6C00)),
                3 to ("Bom/Disponível" to Color(0xFFE8F5E9) to Color(0xFF2E7D32))
            )

            options.forEach { (value, styling) ->
                val (textAndBg, textColor) = styling
                val (text, bgColor) = textAndBg
                val isSelected = selectedValue == value

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) bgColor else Color.LightGray.copy(alpha = 0.15f))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) textColor else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onValueSelected(value) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) textColor else TextMediumGray
                    )
                }
            }
        }
    }
}
