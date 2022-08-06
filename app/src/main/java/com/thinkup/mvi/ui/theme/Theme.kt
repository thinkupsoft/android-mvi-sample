package com.thinkup.mvi.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import com.thinkup.mvi.compose.TkupAppState
import com.thinkup.mvi.compose.getColorScheme
import com.thinkup.mvi.compose.rememberTkupAppState

val DarkColorScheme = darkColorScheme(
    primary = PrimaryCyan,
    secondary = PrimaryRed,
    tertiary = PrimaryDark,
    background = Background
)

val LightColorScheme = lightColorScheme(
    primary = PrimaryCyan,
    secondary = PrimaryRed,
    tertiary = PrimaryDark,
    background = Background
)

@Composable
fun ThinkUpTheme(
    appState: TkupAppState = rememberTkupAppState(),
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = getColorScheme(darkTheme)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}