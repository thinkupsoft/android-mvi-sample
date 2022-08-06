package com.thinkup.mvi.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.thinkup.common.empty
import com.thinkup.mvi.R
import com.thinkup.mvi.compose.TkupAppState
import com.thinkup.mvi.compose.rememberTkupAppState
import com.thinkup.mvi.ui.theme.Background
import com.thinkup.mvi.ui.theme.DIMEN_BUTTON
import com.thinkup.mvi.ui.theme.DIMEN_HEADER_IMAGE
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.darkTitle
import com.thinkup.mvi.ui.theme.screenMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class TkupCustomSuccessConfig(
    val visible: Boolean = true,
    val title: String = String.empty(),
    val message: String = String.empty(),
    val timeElapsed: Long = 3500L
)

@Composable
fun TkupCustomSuccess(
    appState: TkupAppState = rememberTkupAppState(),
    config: TkupCustomSuccessConfig,
    onHideAlertMessage: () -> Unit
) {
    if (config.visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Background)
                .focusable()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {

                Spacer(modifier = Modifier.size(DIMEN_BUTTON))
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.tkup_success))
                LottieAnimation(
                    composition,
                    modifier = Modifier
                        .size(DIMEN_HEADER_IMAGE)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.size(DIMEN_XX_NORMAL))
                Text(text = config.title, style = Typography.darkTitle, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.size(DIMEN_XX_NORMAL))
                Text(
                    text = config.message,
                    textAlign = TextAlign.Center,
                    style = Typography.screenMessage,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = DIMEN_XX_NORMAL)
                )
            }
        }
        SideEffect {
            appState.coroutineScope.launch {
                delay(config.timeElapsed)
                onHideAlertMessage()
            }
        }
    }
}