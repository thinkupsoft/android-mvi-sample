package com.thinkup.mvi.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.theme.DIMEN_NO_SPACE
import com.thinkup.mvi.ui.theme.DIMEN_SMEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_X_SMEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_X_SMALL
import com.thinkup.mvi.ui.theme.Gray1
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.utils.ResourcesHelper

data class TkupPaddings(
    val start: Dp = DIMEN_NO_SPACE,
    val end: Dp = DIMEN_NO_SPACE,
    val top: Dp = DIMEN_NO_SPACE,
    val bottom: Dp = DIMEN_NO_SPACE
)


@Composable
fun rememberResourceHelper() = ResourcesHelper(LocalContext.current)

@Composable
fun NoInternetState(onClick: () -> Unit = {}) {
    TkupEmptyView(
        config = TkupEmptyViewConfig(
            message = stringResource(id = R.string.no_internet),
            icon = R.drawable.tkup_no_internet,
            buttonConfig = TkupButtonConfig(
                background = Gray1,
                text = stringResource(id = R.string.retry),
                paddings = TkupPaddings(DIMEN_SMEDIUM, DIMEN_SMEDIUM, DIMEN_TRIPLE_NORMAL, DIMEN_XX_NORMAL),
            ),
            onButtonClick = onClick
        )
    )
}

@Composable
fun FooterLoadingState() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        TkupDotsLoading(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(all = DIMEN_SMEDIUM),
            config = TkupDotsLoadingConfig(
                items = listOf(
                    TkupDotItem(circleSize = DIMEN_X_SMALL),
                    TkupDotItem(circleSize = DIMEN_X_SMALL),
                    TkupDotItem(circleSize = DIMEN_X_SMALL)
                )
            )
        )
    }
}

@Composable
fun FooterErrorState() {
    Box(
        modifier = Modifier
            .background(Gray1, RoundedCornerShape(DIMEN_XX_NORMAL))
            .padding(vertical = DIMEN_X_SMEDIUM),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.retry),
            style = Typography.labelMedium,
            modifier = Modifier
                .padding(horizontal = DIMEN_DOUBLE_NORMAL)
                .align(Alignment.Center)
        )
    }
}