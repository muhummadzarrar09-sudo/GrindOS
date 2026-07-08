package com.grindos.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val GrindOSDarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = TextPrimary,
    primaryContainer = ElectricBlueDark,
    onPrimaryContainer = TextPrimary,
    secondary = NeonGreen,
    onSecondary = DarkBackground,
    secondaryContainer = NeonGreenDark,
    onSecondaryContainer = TextPrimary,
    tertiary = HypeOrange,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,
    outline = DarkElevated,
    outlineVariant = DarkElevated,
    error = DangerRed,
    onError = TextPrimary,
    errorContainer = DangerRed.copy(alpha = 0.2f),
    onErrorContainer = DangerRed,
    inverseSurface = TextPrimary,
    inverseOnSurface = DarkBackground,
    inversePrimary = ElectricBlueDark,
    scrim = DarkBackground.copy(alpha = 0.7f)
)

@Composable
fun GrindOSTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = GrindOSDarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkBackground.toArgb()
            window.navigationBarColor = DarkBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = GrindOSTypography,
        content = content
    )
}
