package com.example.embytv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color(0xFF000000),
    secondary = Color(0xFF03DAC6),
    onSecondary = Color(0xFF000000),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFBDBDBD),
    error = Color(0xFFCF6679),
    onError = Color(0xFF000000),
    tertiary = Color(0xFF3700B3)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3700B3),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF0303DC),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF5F5F5),
    onSurface = Color(0xFF000000),
    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF),
    tertiary = Color(0xFF3700B3)
)

@Composable
fun EmbyTVTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography(),
        content = content
    )
}
