package com.tecsup.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = SurfaceWhite,
    primaryContainer = SecondaryGreen,
    onPrimaryContainer = PrimaryGreen,
    secondary = AccentGold,
    onSecondary = TextDark,
    secondaryContainer = SecondaryGreen,
    background = WarmBackground,
    onBackground = TextDark,
    surface = SurfaceWhite,
    onSurface = TextDark,
    surfaceVariant = SecondaryGreen,
    onSurfaceVariant = TextDark,
    error = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = SurfaceWhite,
    primaryContainer = PrimaryGreen,
    onPrimaryContainer = SecondaryGreen,
    secondary = AccentGold,
    onSecondary = SurfaceWhite,
    background = Color(0xFF121A18),
    onBackground = Color(0xFFE3E3E3),
    surface = Color(0xFF1B2422),
    onSurface = Color(0xFFE3E3E3),
    surfaceVariant = Color(0xFF263B36),
    onSurfaceVariant = Color(0xFFE3E3E3)
)

@Composable
fun LabintegradorMerinoTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
