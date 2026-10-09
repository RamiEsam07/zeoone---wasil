package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AdminDarkColorScheme = darkColorScheme(
    primary = BrandBlueLight,
    onPrimary = Color.White,
    primaryContainer = Slate800,
    onPrimaryContainer = Color.White,
    secondary = Slate400,
    onSecondary = Slate950,
    background = Slate950,
    surface = Slate900,
    surfaceVariant = Slate800,
    onSurface = Slate50,
    onSurfaceVariant = Slate300,
    outline = Slate700,
    error = StatusDanger,
    onError = Color.White
)

private val AdminLightColorScheme = lightColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    primaryContainer = BrandBlueContainer,
    onPrimaryContainer = BrandBlueDark,
    secondary = Slate700,
    onSecondary = Color.White,
    background = Slate50,
    surface = Color.White,
    surfaceVariant = Slate100,
    onSurface = Slate900,
    onSurfaceVariant = Slate600,
    outline = Slate200,
    error = StatusDanger,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AdminDarkColorScheme else AdminLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
