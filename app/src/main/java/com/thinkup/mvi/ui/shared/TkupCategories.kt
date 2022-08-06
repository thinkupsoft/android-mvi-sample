package com.thinkup.mvi.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.theme.Background
import com.thinkup.mvi.ui.theme.ThinkUpTheme
import com.thinkup.mvi.ui.theme.DIMEN_HEADER_IMAGE
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_X_SMALL
import com.thinkup.mvi.ui.theme.PrimaryCyan
import com.thinkup.mvi.ui.theme.PrimaryCyan18
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.smallHeader
import com.thinkup.common.empty

data class TkupCategoryTagConfig(
    val id: Int = 0,
    val label: String = String.empty(),
    val closable: Boolean = false,
    val unselectedBackground: Color = PrimaryCyan18,
    val unselectedTextStyle: TextStyle = Typography.smallHeader,
    val selectedTextStyle: TextStyle = Typography.labelMedium,
    val selectedBackground: Color = PrimaryCyan,
    val selected: Boolean = false,
    val onCheck: (Boolean, Int) -> Unit = { _, _ -> }
)

@Composable
fun TkupCategoriesContainer(
    modifier: Modifier,
    tags: List<TkupCategoryTagConfig> = emptyList()
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = DIMEN_XX_NORMAL)
            .background(Background),
        crossAxisAlignment = FlowCrossAxisAlignment.Start,
        crossAxisSpacing = DIMEN_X_SMALL,
        mainAxisAlignment = MainAxisAlignment.Start,
        mainAxisSize = SizeMode.Expand,
        mainAxisSpacing = DIMEN_X_SMALL
    ) {
        tags.sortedBy { !it.selected }.forEach {
            Box(
                contentAlignment = Alignment.Center
            ) {
                TkupCategoryTag(it)
            }
        }
    }
}

@Composable
fun TkupCategoryTag(config: TkupCategoryTagConfig) {
    val backgroundColor = if (config.selected) config.selectedBackground else config.unselectedBackground
    val textStyle = if (config.selected) config.selectedTextStyle else config.unselectedTextStyle
    val closeColor = if (config.selected) config.selectedTextStyle.color else config.unselectedTextStyle.color

    IconToggleButton(
        checked = config.selected,
        onCheckedChange = { config.onCheck(it, config.id) },
        modifier = Modifier
            .height(DIMEN_TRIPLE_NORMAL)
            .width(IntrinsicSize.Max)
            .widthIn(DIMEN_TRIPLE_NORMAL, DIMEN_HEADER_IMAGE)
            .background(backgroundColor, RoundedCornerShape(DIMEN_XX_NORMAL))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(
                text = config.label,
                modifier = Modifier.padding(start = DIMEN_XX_NORMAL, end = if (config.closable) DIMEN_X_SMALL else DIMEN_XX_NORMAL),
                style = textStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (config.closable) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.tkup_tag_close),
                    contentDescription = "close",
                    modifier = Modifier.padding(end = DIMEN_XX_NORMAL),
                    colorFilter = ColorFilter.tint(closeColor)
                )
            }
        }
    }
}

@Preview
@Composable
fun TkupCategoriesContainerPreview() {
    ThinkUpTheme {
        TkupCategoriesContainer(
            Modifier,
            listOf(
                TkupCategoryTagConfig(
                    label = "Comedy"
                ),
                TkupCategoryTagConfig(
                    label = "Drama"
                ),
                TkupCategoryTagConfig(
                    label = "Thriller",
                    selected = true
                ),
                TkupCategoryTagConfig(
                    label = "Classic"
                )
            )
        )
    }
}