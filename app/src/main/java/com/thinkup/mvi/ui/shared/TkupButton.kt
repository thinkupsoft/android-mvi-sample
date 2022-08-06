package com.thinkup.mvi.ui.shared

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.theme.DIMEN_X_SMEDIUM
import com.thinkup.mvi.ui.theme.DIMEN_BUTTON
import com.thinkup.mvi.ui.theme.Gray1
import com.thinkup.mvi.ui.theme.PrimaryCyan
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.common.empty
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL

data class TkupButtonConfig(
    val text: String = String.empty(),
    @DrawableRes val icon: Int? = null,
    val background: Color = PrimaryCyan,
    val textColor: Color = Color.White,
    val disableBackground: Color = Gray1,
    val disableTextColor: Color = Color.White,
    val heightDp: Dp = DIMEN_BUTTON,
    val roundedDp: Dp = DIMEN_DOUBLE_NORMAL,
    val paddings: TkupPaddings = TkupPaddings(),
    val enabled: Boolean = true
)

@Composable
fun TkupButton(
    modifier: Modifier,
    config: TkupButtonConfig,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier.padding(
            top = config.paddings.top,
            bottom = config.paddings.bottom
        )
    ) {
        Button(
            shape = RoundedCornerShape(config.roundedDp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = config.background,
                contentColor = config.textColor,
                disabledBackgroundColor = config.disableBackground,
                disabledContentColor = config.disableTextColor
            ),
            modifier = modifier
                .height(config.heightDp)
                .fillMaxWidth()
                .padding(
                    start = config.paddings.start,
                    end = config.paddings.end
                ),
            enabled = config.enabled,
            onClick = { onClick?.invoke() }
        ) {
            Text(text = config.text, style = Typography.labelMedium, color = config.textColor)
            config.icon?.let {
                Image(
                    modifier = Modifier.padding(start = DIMEN_X_SMEDIUM),
                    imageVector = ImageVector.vectorResource(id = config.icon),
                    contentDescription = "icon"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TkupButtonPreview() {
    TkupButton(Modifier, TkupButtonConfig(
        text = "Sample",
        icon = R.drawable.tkup_new_image_medium
    ))
}