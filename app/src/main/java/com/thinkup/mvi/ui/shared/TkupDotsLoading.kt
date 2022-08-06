package com.thinkup.mvi.ui.shared

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.thinkup.mvi.ui.theme.ThinkUpTheme
import com.thinkup.mvi.ui.theme.DIMEN_SMEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_X_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.PrimaryCyan
import kotlinx.coroutines.delay

data class TkupDotsLoadingConfig(
    val items: List<TkupDotItem> = emptyList(),
    val spaceBetween: Dp = DIMEN_SMEDIUM,
    val travelDistance: Dp = DIMEN_X_NORMAL
)

data class TkupDotItem(
    val circleColor: Color = PrimaryCyan,
    val circleSize: Dp = DIMEN_XX_NORMAL,
)

@Composable
fun TkupDotsLoading(
    modifier: Modifier = Modifier,
    config: TkupDotsLoadingConfig
) {
    val circles = config.items.map {
        Pair(it, remember { Animatable(initialValue = 0f) })
    }

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L)
            animatable.second.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    val distance = with(LocalDensity.current) { config.travelDistance.toPx() }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(config.spaceBetween)
    ) {
        circles.forEach { value ->
            Box(
                modifier = Modifier
                    .size(value.first.circleSize)
                    .graphicsLayer {
                        translationY = -value.second.value * distance
                    }
                    .background(
                        color = value.first.circleColor,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xffffff)
@Composable
fun TkupDotsLoadingPreview() {
    ThinkUpTheme {
        TkupDotsLoading(
            config = TkupDotsLoadingConfig(
                items = listOf(
                    TkupDotItem(),
                    TkupDotItem(),
                    TkupDotItem()
                )
            )
        )
    }
}