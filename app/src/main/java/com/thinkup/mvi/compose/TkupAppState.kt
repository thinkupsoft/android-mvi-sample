package com.thinkup.mvi.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.thinkup.mvi.ui.theme.DarkColorScheme
import com.thinkup.mvi.ui.theme.LightColorScheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberTkupAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    darkTheme: Boolean = isSystemInDarkTheme()
) = remember(navController) {

    TkupAppState(
        navController = navController,
        coroutineScope = coroutineScope,
        colorScheme = getColorScheme(darkTheme = darkTheme)
    )
}

fun getColorScheme(darkTheme: Boolean): ColorScheme {
    return when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
}

class TkupAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val colorScheme: ColorScheme
)


