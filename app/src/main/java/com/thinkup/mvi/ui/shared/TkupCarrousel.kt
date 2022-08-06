package com.thinkup.mvi.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.thinkup.mvi.ui.theme.DIMEN_NO_SPACE
import com.thinkup.mvi.ui.theme.DIMEN_CARROUSEL
import com.thinkup.mvi.ui.theme.DIMEN_X_SMALL
import com.thinkup.mvi.ui.theme.PrimaryCyan
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
data class TkupCarrouselConfig(
    val state: PagerState,
    val horizontalPadding: Dp = DIMEN_CARROUSEL,
    val itemSpacing: Dp = DIMEN_X_SMALL,
    val startPercentage: Float = 0.85f,
    val stopPercentage: Float = 1.0f,
    val startAlpha: Float = 0.70f,
    val stopAlpha: Float = 1.0f,
    val content: List<@Composable () -> Unit> = emptyList()
)

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TkupCarrousel(modifier: Modifier = Modifier, config: TkupCarrouselConfig) {

    HorizontalPager(
        count = config.content.size,
        state = config.state,
        contentPadding = PaddingValues(horizontal = config.horizontalPadding),
        modifier = modifier.fillMaxWidth(),
        itemSpacing = config.itemSpacing
    ) { page ->

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent,
                contentColor = PrimaryCyan
            ),
            shape = RoundedCornerShape(DIMEN_NO_SPACE),
            modifier = Modifier
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                    lerp(
                        start = config.startPercentage.dp,
                        stop = config.stopPercentage.dp,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale.value
                        scaleY = scale.value
                    }
                    alpha = lerp(
                        start = config.startAlpha.dp,
                        stop = config.stopAlpha.dp,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).value
                }
                .fillMaxWidth()
                .background(Color.Transparent)
        ) {
            config.content[page]()
        }
    }
}