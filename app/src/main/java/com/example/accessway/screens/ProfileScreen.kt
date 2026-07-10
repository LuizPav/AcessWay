package com.example.accessway.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accessway.ui.theme.BackgroundWhite
import com.example.accessway.ui.theme.LogoBlue
import com.example.accessway.ui.theme.TextDarkGray
import com.example.accessway.ui.theme.TextLightGray
import com.example.accessway.ui.theme.TextMediumGray
import com.example.accessway.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onOpenMenu: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val profile = viewModel.profileState
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Estado para o nome no diálogo
    var newName by remember(profile.name) { mutableStateOf(profile.name) }

    Box(modifier = Modifier.fillMaxSize()) {

        // --- DIÁLOGO DE EDIÇÃO ---
        if (viewModel.isEditingName) {
            AlertDialog(
                onDismissRequest = { viewModel.toggleEditNameDialog(false) },
                title = { Text("Editar Nome") },
                text = {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nome completo") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.updateName(newName)
                        viewModel.toggleEditNameDialog(false)
                    }) {
                        Text("Salvar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.toggleEditNameDialog(false) }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (viewModel.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundWhite)
                    .statusBarsPadding()
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onOpenMenu) { Icon(Icons.Default.Menu, null, tint = LogoBlue) }
                    Text("Meu Perfil", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = LogoBlue, modifier = Modifier.padding(start = 16.dp))
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp)
                ) {
                    // Card Usuário
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccountCircle, null, tint = LogoBlue, modifier = Modifier.size(64.dp))
                            Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                                Text(profile.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDarkGray)
                                Text(profile.email, fontSize = 14.sp, color = TextMediumGray)
                            }
                            IconButton(onClick = {
                                newName = profile.name
                                viewModel.toggleEditNameDialog(true)
                            }) {
                                Icon(Icons.Default.Edit, "Editar", tint = LogoBlue)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Preferências de Acessibilidade", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = LogoBlue, modifier = Modifier.padding(bottom = 8.dp))

                    // Cards de Preferências
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            PreferenceSwitchRow("Rotas para Cadeirantes", "Prioriza rampas e elevadores.", Icons.Default.Accessible, profile.needsWheelchair) { viewModel.updateWheelchair(it) }
                            HorizontalDivider()
                            PreferenceSwitchRow("Presença de Piso Tátil", "Alerta sinalização direcional.", Icons.Default.Visibility, profile.needsTactilePaving) { viewModel.updateTactilePaving(it) }
                            HorizontalDivider()
                            PreferenceSwitchRow("Alertas Sonoros", "Cruzamentos com sinal sonoro.", Icons.Default.Notifications, profile.needsAudioAlerts) { viewModel.updateAudioAlerts(it) }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Configurações do Mapa", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = LogoBlue, modifier = Modifier.padding(bottom = 8.dp))

                    // Card de Configurações do Mapa (Raio de busca)
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            PreferenceSliderRow(
                                title = "Raio de Busca de Paradas",
                                desc = "Define o raio em metros para buscar paradas ao redor.",
                                icon = Icons.Default.Search,
                                value = profile.searchRadius
                            ) { viewModel.updateSearchRadius(it) }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Configurações da Conta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = LogoBlue, modifier = Modifier.padding(bottom = 8.dp))

                    // Card Configs
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        ConfigRow("Segurança e Senha", Icons.Default.Lock) {
                            viewModel.sendPasswordResetEmail { success, message ->
                                scope.launch { snackbarHostState.showSnackbar(message ?: "Erro") }
                            }
                        }
                        HorizontalDivider()
                        ConfigRow("Sair da Conta", Icons.AutoMirrored.Filled.ExitToApp, Color.Red, Color.Red, onLogout)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        // Host do Snackbar
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun HorizontalDivider() {
    Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray.copy(alpha = 0.3f)))
}

@Composable
fun PreferenceSwitchRow(title: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = LogoBlue, modifier = Modifier.size(24.dp))
        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = TextDarkGray)
            Text(desc, fontSize = 12.sp, color = TextMediumGray)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun ConfigRow(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconColor: Color = LogoBlue, titleColor: Color = TextDarkGray, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = iconColor, modifier = Modifier.size(22.dp))
        Text(title, modifier = Modifier.weight(1f).padding(start = 16.dp), color = titleColor, fontWeight = FontWeight.SemiBold)
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = TextLightGray)
    }
}

@Composable
fun PreferenceSliderRow(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    var sliderValue by remember(value) { mutableStateOf(value.toFloat()) }
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = LogoBlue, modifier = Modifier.size(24.dp))
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(title, fontWeight = FontWeight.Bold, color = TextDarkGray)
                Text(desc, fontSize = 12.sp, color = TextMediumGray)
            }
            Text(
                text = "${sliderValue.toInt()}m",
                fontWeight = FontWeight.Bold,
                color = LogoBlue,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            onValueChangeFinished = {
                onValueChange(sliderValue.toInt())
            },
            valueRange = 100f..5000f,
            steps = 48
        )
    }
}