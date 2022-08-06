package com.thinkup.mvi.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_TRIPLE_NORMAL
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.darkTitle

@Composable
fun TkupNavigationTitle(
    title: String,
    showBAckArrow: Boolean = true,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = DIMEN_TRIPLE_NORMAL, start = DIMEN_DOUBLE_NORMAL, end = DIMEN_DOUBLE_NORMAL)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        if (showBAckArrow) Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.tkup_navigation_icon),
            contentDescription = "navIcon",
            alignment = Alignment.CenterStart,
            modifier = Modifier
                .padding(end = DIMEN_XX_NORMAL)
                .clickable { onBackClicked() }
        )
        Text(
            text = title,
            style = Typography.darkTitle,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun TkupNavigationTitlePreview() {
    Box(
        Modifier
            .fillMaxSize()
    ) {
        TkupNavigationTitle("Title") {}
    }
}