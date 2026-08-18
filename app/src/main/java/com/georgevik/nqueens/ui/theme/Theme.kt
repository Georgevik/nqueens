package com.georgevik.nqueens.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    surface = Indigo6,
    surfaceDim = Indigo6,
    surfaceBright = Indigo24,
    surfaceContainerLowest = Indigo4,
    surfaceContainerLow = Indigo10A,
    surfaceContainer = Indigo12,
    surfaceContainerHigh = Indigo17A,
    surfaceContainerHighest = Indigo22,
    onSurface = Indigo90A,
    onSurfaceVariant = Indigo80A,
    inverseSurface = Indigo90A,
    inverseOnSurface = Indigo20A,
    outline = Neutral60,
    outlineVariant = Neutral30,
    surfaceTint = Indigo80B,
    primary = Indigo80B,
    onPrimary = Indigo20B,
    primaryContainer = Indigo60,
    onPrimaryContainer = Indigo17B,
    inversePrimary = Indigo40,
    secondary = Indigo80C,
    onSecondary = Indigo20C,
    secondaryContainer = Indigo31,
    onSecondaryContainer = Indigo75,
    tertiary = Orange80,
    onTertiary = Orange20,
    tertiaryContainer = Orange60,
    onTertiaryContainer = Orange17,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    primaryFixed = Indigo90B,
    primaryFixedDim = Indigo80B,
    onPrimaryFixed = Indigo10B,
    onPrimaryFixedVariant = Indigo30A,
    secondaryFixed = Indigo90A,
    secondaryFixedDim = Indigo80C,
    onSecondaryFixed = Indigo10C,
    onSecondaryFixedVariant = Indigo30B,
    tertiaryFixed = Orange90,
    tertiaryFixedDim = Orange80,
    onTertiaryFixed = Orange10,
    onTertiaryFixedVariant = Orange30,
    background = Indigo6,
    onBackground = Indigo90A,
    surfaceVariant = Indigo22,
)

@Composable
fun NQueensTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
