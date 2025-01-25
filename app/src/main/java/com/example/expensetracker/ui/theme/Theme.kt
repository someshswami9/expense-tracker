package com.example.expensetracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF8C75),
    secondary = Color(0xFFFFD1C7),
    background = Color(0xFFEDEDED).copy(alpha = 0.56f),
    error = Color(0xFFF81E1E),
    surface = Color(0xFFFFC8BD),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onError = Color.White,
    onSurface = Color.Black
)

@Composable
fun ExpenseTrackerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
} 