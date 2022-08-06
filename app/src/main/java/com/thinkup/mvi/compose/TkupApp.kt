package com.thinkup.mvi.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.thinkup.mvi.ui.theme.ThinkUpTheme

@Composable
fun TkupApplication(appState: TkupAppState = rememberTkupAppState()) {
    TkupScreen(appState = appState) {
        Navigation(appState = appState)
    }
}

@Composable
fun TkupScreen(appState: TkupAppState, content: @Composable () -> Unit) {
    ThinkUpTheme(appState = appState) {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}