package com.thinkup.mvi.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.theme.DIMEN_XX_SMALL
import com.thinkup.mvi.ui.theme.PrimaryCyan
import com.thinkup.mvi.ui.theme.PrimaryCyan50
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.White50
import com.thinkup.mvi.ui.theme.screenMessage

data class TkupCustomLoaderConfig(
    val visible: Boolean = true,
    val type: Type = Type.FULL,
    val text: String? = null,
    val backgroundColor: Color = PrimaryCyan50
)

enum class Type { FULL, SOFT, NONE }

@Composable
fun TkupCustomLoader(
    config: TkupCustomLoaderConfig
) {
    if (config.visible) {
        val background = if (config.type == Type.FULL) White50 else config.backgroundColor
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = background)
                .focusable()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { }
        ) {
            when (config.type) {
                Type.SOFT -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = PrimaryCyan,
                    strokeWidth = DIMEN_XX_SMALL
                )
                Type.FULL -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        verticalArrangement = Arrangement.Center
                    ) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.tkup_loader))
                        LottieAnimation(
                            composition,
                            iterations = LottieConstants.IterateForever
                        )
                        config.text?.let {
                            Text(text = it, style = Typography.screenMessage, modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                }
                else -> {}
            }
        }
    }
}