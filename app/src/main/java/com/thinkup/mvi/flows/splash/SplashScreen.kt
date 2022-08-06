package com.thinkup.mvi.flows.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thinkup.mvi.R
import com.thinkup.mvi.compose.Flows
import com.thinkup.mvi.compose.TkupAppState
import com.thinkup.mvi.compose.rememberTkupAppState
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.PrimaryDark
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(appState: TkupAppState = rememberTkupAppState()) {
    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
    systemUiController.isNavigationBarVisible = false

    Box(
        modifier = Modifier
            .background(color = PrimaryDark)
            .fillMaxHeight()
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.tkup_logo),
            contentDescription = "logo",
            alignment = Alignment.Center
        )

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.tkup_footer),
            contentDescription = "footer",
            alignment = Alignment.BottomCenter,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = DIMEN_XX_NORMAL)
        )
    }

    LaunchedEffect(null) {
        delay(2500)
        appState.navController.navigate(Flows.HOME.route) {
            popUpTo(Flows.SPLASH.route) { inclusive = true }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    SplashScreen()
}