package com.example.accessway.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// 🌙 Configuração de Cores para o Tema Escuro
private val DarkColorScheme = darkColorScheme(
    primary = LogoBlue,               // Azul da logo como cor principal
    secondary = LogoGreen,             // Verde da logo como cor secundária
    tertiary = CustomGreen,            // Verde customizado
    background = Color(0xFF121212),    // Fundo escuro padrão
    surface = Color(0xFF1E1E1E),       // Superfícies escuras (cards, sheets)
    error = ErrorRedViva,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

// ☀️ Configuração de Cores para o Tema Claro (Seu padrão atual)
private val LightColorScheme = lightColorScheme(
    primary = PrimaryButtonBlue,       // O azul do seu botão de login vira a cor primária do app
    secondary = LogoGreen,             // O verde da logo vira a secundária
    tertiary = CustomGreen,            // O verde 0xFF43A047 como cor terciária
    background = BackgroundWhite,      // Força o fundo branco limpo nas telas
    surface = BackgroundWhite,         // Cards e menus laterais brancos por padrão
    error = ErrorRedViva,
    onPrimary = Color.White,           // Texto sobre a cor primária será branco
    onSecondary = Color.White,         // Texto sobre a cor secundária
    onBackground = TextDarkGray,       // Cor padrão para textos normais sobre o fundo branco
    onSurface = TextDarkGray           // Cor padrão para textos sobre superfícies
)

@Composable
fun AccessWayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 🛠️ Mudamos para 'false' por padrão para o Android não ignorar suas cores
    // e pintar o app com a cor do papel de parede do usuário.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}