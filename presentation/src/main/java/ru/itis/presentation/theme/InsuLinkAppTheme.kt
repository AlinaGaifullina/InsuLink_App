package ru.itis.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFF5F5F5),
    onPrimary = Color(0xFF000000),
    secondary = Color(0xFFCCB6AC),
    onSecondary = Color(0xFF3E4A36),
    tertiary = Color(0xFFB8B156),
    onTertiary = Color(0xFF000000)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFF5F5F5),
    onPrimary = Color(0xFF464545),
    secondary = Color(0xFF78B162),
    onSecondary = Color(0xFFF3F7EF),
    tertiary = Color(0xFFF3C96F),
    onTertiary = Color(0xFF564E3D),
    primaryContainer = Color(0xFFE7EDE3),
    secondaryContainer = Color(0xFFF5EFE2),
    surfaceTint = Color(0xFF596943),
    surface = Color(0xFFB1433B)
)

@Composable
fun InsuLinkAppTheme(
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

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primary.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//        }
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}