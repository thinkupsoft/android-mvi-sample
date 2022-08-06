package com.thinkup.mvi.ui.shared

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun IndicatorShape(count: Int, modifier: Modifier, config: IndicatorConfig) {
    IndicatorBox(shape = RoundedCornerShape(config.roundedDp), modifier, config, count)
}

@Composable
fun IndicatorBox(shape: Shape, modifier: Modifier, config: IndicatorConfig, count: Int) {
    val widthDps = LocalConfiguration.current.screenWidthDp
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val width = config.width
        val padding = ((((widthDps / count) / 2)) - (width.value / 2))
        Box(
            modifier = Modifier
                .padding(start = padding.dp, end = padding.dp)
                .width(width)
                .height(config.height)
                .clip(shape)
                .background(config.color)
        )
    }
}

@Composable
fun BottomBarIndicator(modifier: Modifier, config: IndicatorConfig, count: Int, selectedIndex: Int) {
    val coroutineScope = rememberCoroutineScope()
    val widthDps = LocalConfiguration.current.screenWidthDp
    val currentOffsetX = (widthDps / count) * selectedIndex
    val offsetX = remember { Animatable(0F) }

    if (config.show) {
        IndicatorShape(
            count,
            modifier
                .offset(x = offsetX.value.dp, y = config.paddingTop),
            config
        )

        LaunchedEffect(selectedIndex) {
            coroutineScope.launch {
                offsetX.animateTo(
                    targetValue = currentOffsetX.toFloat(),
                    animationSpec = tween(
                        durationMillis = config.animDuration,
                        delayMillis = config.animDelay
                    )
                )
            }
        }
    }
}