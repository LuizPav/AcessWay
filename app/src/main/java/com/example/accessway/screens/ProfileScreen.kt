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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.accessway.ui.theme.BackgroundWhite
import com.example.accessway.ui.theme.LogoBlue
import com.example.accessway.ui.theme.LogoGreen
import com.example.accessway.ui.theme.TextDarkGray
import com.example.accessway.ui.theme.TextLightGray
import com.example.accessway.ui.theme.TextMediumGray
import com.example.accessway.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    onOpenMenu: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val profile = viewModel.profileState
    val scrollState = rememberScrollState()

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
                text = "Meu Perfil",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = LogoBlue
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- USER INFO CARD ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Foto de perfil",
                        tint = LogoBlue,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = profile.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDarkGray
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = profile.email,
                            fontSize = 14.sp,
                            color = TextMediumGray
                        )
                    }
                    IconButton(
                        onClick = {
                            // Mock editing user name
                            viewModel.updateName(if (profile.name == "Luiz Pavão") "Luiz Henrique Pavão" else "Luiz Pavão")
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.LightGray.copy(alpha = 0.2f))
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar nome",
                            tint = LogoBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- ACCESSIBILITY PREFERENCES SECTION ---
            Text(
                text = "Preferências de Acessibilidade",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = LogoBlue,
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Switch 1: Wheelchair
                    PreferenceSwitchRow(
                        title = "Rotas para Cadeirantes",
                        description = "Prioriza caminhos com rampas, elevadores e rebaixamento de calçada.",
                        icon = Icons.Default.Accessible,
                        checked = profile.needsWheelchair,
                        onCheckedChange = { viewModel.updateWheelchair(it) }
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f))
                    )

                    // Switch 2: Tactile Paving
                    PreferenceSwitchRow(
                        title = "Presença de Piso Tátil",
                        description = "Alerta rotas que contam com sinalização tátil direcional e de alerta.",
                        icon = Icons.Default.Visibility,
                        checked = profile.needsTactilePaving,
                        onCheckedChange = { viewModel.updateTactilePaving(it) }
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f))
                    )

                    // Switch 3: Audio Alerts
                    PreferenceSwitchRow(
                        title = "Alertas Sonoros",
                        description = "Informa cruzamentos equipados com sinal sonoro ou alertas falados.",
                        icon = Icons.Default.Notifications,
                        checked = profile.needsAudioAlerts,
                        onCheckedChange = { viewModel.updateAudioAlerts(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- ACCOUNT CONFIGS SECTION ---
            Text(
                text = "Configurações da Conta",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = LogoBlue,
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ConfigRow(
                        title = "Segurança e Senha",
                        icon = Icons.Default.Lock,
                        onClick = {}
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f))
                    )
                    ConfigRow(
                        title = "Termos de Uso e Privacidade",
                        icon = Icons.Default.Info,
                        onClick = {}
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f))
                    )
                    ConfigRow(
                        title = "Excluir Conta",
                        icon = Icons.Default.DeleteForever,
                        iconColor = Color.Red,
                        titleColor = Color.Red,
                        onClick = {}
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f))
                    )
                    ConfigRow(
                        title = "Sair da Conta",
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        iconColor = Color.Red,
                        titleColor = Color.Red,
                        onClick = onLogout
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PreferenceSwitchRow(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(LogoBlue.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LogoBlue,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextDarkGray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = TextMediumGray,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = LogoGreen,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Composable
fun ConfigRow(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color = LogoBlue,
    titleColor: Color = TextDarkGray,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = titleColor,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextLightGray,
            modifier = Modifier.size(20.dp)
        )
    }
}