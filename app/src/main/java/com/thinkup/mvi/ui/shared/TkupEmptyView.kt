package com.thinkup.mvi.ui.shared

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.theme.ThinkUpTheme
import com.thinkup.mvi.ui.theme.DIMEN_SMEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_X_BIG
import com.thinkup.mvi.ui.theme.Gray1
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.darkTitle
import com.thinkup.mvi.ui.theme.screenMessage

data class TkupEmptyViewConfig(
    val title: String? = null,
    val titleStyle: TextStyle = Typography.darkTitle,
    val message: String? = null,
    val messageStyle: TextStyle = Typography.screenMessage,
    @DrawableRes val icon: Int? = null,
    val iconColor: Color? = null,
    val buttonConfig: TkupButtonConfig? = null,
    val paddings: TkupPaddings = TkupPaddings(start = DIMEN_X_BIG, end = DIMEN_X_BIG, top = DIMEN_TRIPLE_NORMAL, bottom = DIMEN_NORMAL),
    val onButtonClick: () -> Unit = {}
)

@Composable
fun TkupEmptyView(modifier: Modifier = Modifier, config: TkupEmptyViewConfig) {
    Column(
        modifier = modifier.padding(
            start = config.paddings.start,
            end = config.paddings.end,
            top = config.paddings.top,
            bottom = config.paddings.bottom,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        config.icon?.let {
            Image(
                imageVector = ImageVector.vectorResource(id = it),
                contentDescription = "icon",
                colorFilter = if (config.iconColor != null) ColorFilter.tint(config.iconColor)
                else null
            )
        }
        config.title?.let {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                style = config.titleStyle,
                modifier = Modifier.padding(top = DIMEN_DOUBLE_NORMAL)
            )
        }
        config.message?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = DIMEN_DOUBLE_NORMAL),
                textAlign = TextAlign.Center,
                style = config.messageStyle,
            )
        }
        config.buttonConfig?.let {
            TkupButton(modifier = Modifier, config = it) { config.onButtonClick() }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xffffff)
@Composable
fun TkupEmptyViewPreview() {
    ThinkUpTheme {
        TkupEmptyView(
            config = TkupEmptyViewConfig(
                title = "Empty View",
                message = "You don’t have any registered yet!",
                icon = R.drawable.tkup_logo,
                buttonConfig = TkupButtonConfig(
                    background = Gray1,
                    text = "NEW",
                    icon = R.drawable.tkup_new_image_medium,
                    paddings = TkupPaddings(DIMEN_SMEDIUM, DIMEN_SMEDIUM, DIMEN_TRIPLE_NORMAL, DIMEN_XX_NORMAL)
                )
            )
        )
    }
}