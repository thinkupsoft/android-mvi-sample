package com.thinkup.mvi.ui.shared

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.thinkup.mvi.ui.theme.Background
import com.thinkup.mvi.ui.theme.DIMEN_NO_SPACE
import com.thinkup.mvi.ui.theme.DIMEN_MIN_SPACE
import com.thinkup.mvi.ui.theme.DIMEN_XXX_SMALL
import com.thinkup.mvi.ui.theme.Gray2
import com.thinkup.mvi.ui.theme.PrimaryCyan
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.tabSelectedHeader
import com.thinkup.mvi.ui.theme.tabUnselectedHeader
import com.thinkup.common.empty
import kotlinx.coroutines.launch

data class TkupTabLayoutConfig(
    val items: List<TkupTabItem> = emptyList(),
    val backgroundColor: Color = Background,
    val useBottomSeparator: Boolean = true,
    val separatorColor: Color = Gray2,
    val indicatorPadding: Dp = DIMEN_NO_SPACE,
    val indicatorColor: Color = PrimaryCyan,
    val indicatorHeight: Dp = DIMEN_XXX_SMALL,
    val textStyleSelected: TextStyle = Typography.tabSelectedHeader,
    val textStyleUnselected: TextStyle = Typography.tabUnselectedHeader,
)

data class TkupTabItem(
    val title: String = String.empty(),
    var selected: Boolean = false,
    val enabled: Boolean = true,
    @DrawableRes val icon: Int? = null,
    val onClick: () -> Unit = {},
    val content: @Composable () -> Unit = {}
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TkupTabLayout(modifier: Modifier, config: TkupTabLayoutConfig) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = config.backgroundColor,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = config.indicatorColor,
                    height = config.indicatorHeight,
                    modifier = Modifier
                        .pagerTabIndicatorOffset(pagerState, tabPositions)
                        .padding(horizontal = config.indicatorPadding)
                )
            },
            divider = {
                if (config.useBottomSeparator)
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(config.separatorColor)
                            .height(DIMEN_MIN_SPACE)
                    )
            }
        ) {
            config.items.forEachIndexed { index, item ->
                val selected = pagerState.currentPage == index
                Tab(
                    text = { Text(item.title, style = if (selected) config.textStyleSelected else config.textStyleUnselected) },
                    selected = selected,
                    enabled = item.enabled,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    icon = {
                        item.icon?.let {
                            Image(
                                imageVector = ImageVector.vectorResource(id = it),
                                contentDescription = "icon",
                                colorFilter = ColorFilter.tint(
                                    if (selected) config.textStyleSelected.color
                                    else config.textStyleUnselected.color
                                )
                            )
                        }
                    }
                )
            }
        }

        HorizontalPager(
            count = config.items.size,
            state = pagerState,
        ) { page ->
            config.items[page].content()
        }
    }
}
