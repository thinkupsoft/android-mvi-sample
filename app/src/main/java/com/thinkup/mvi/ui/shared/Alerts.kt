package com.thinkup.mvi.ui.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_XXX_SMALL
import com.thinkup.mvi.ui.theme.DIMEN_BUTTON
import com.thinkup.mvi.ui.theme.Error
import com.thinkup.mvi.ui.theme.Error10
import com.thinkup.mvi.ui.theme.Success
import com.thinkup.mvi.ui.theme.Success10
import com.thinkup.common.empty
import kotlinx.coroutines.delay

const val ANIMATION_VALUE = 350
const val HIDE_DELAY = 3000L

data class AlertMessage(
    val message: String,
    val isSuccess: Boolean = false
)

@Composable
fun ShowAlertMessage(
    alertMessage: AlertMessage?,
    onHideAlertMessage: () -> Unit,
    backgroundContent: @Composable (() -> Unit)? = null,
) {
    val message = rememberSaveable { mutableStateOf(String.empty()) }
    val isSuccess = rememberSaveable { mutableStateOf(false) }

    alertMessage?.message?.let { message.value = it }
    alertMessage?.let { isSuccess.value = it.isSuccess }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        backgroundContent?.invoke()
        Alert(
            show = alertMessage != null,
            message = message.value,
            isSuccess = isSuccess.value
        )
    }

    LaunchedEffect(alertMessage) {
        if (alertMessage != null) {
            delay(HIDE_DELAY)
            onHideAlertMessage()
        }
    }
}

@Composable
private fun Alert(
    show: Boolean,
    message: String,
    isSuccess: Boolean
) {
    val color = if (isSuccess) Success else Error
    val color10 = if (isSuccess) Success10 else Error10
    val image = if (isSuccess) R.drawable.tkup_success_icon else R.drawable.tkup_error_icon

    AnimatedVisibility(
        visible = show,
        modifier = Modifier
            .padding(
                top = DIMEN_NORMAL,
                start = DIMEN_DOUBLE_NORMAL,
                end = DIMEN_DOUBLE_NORMAL
            )
            .height(DIMEN_BUTTON),
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = ANIMATION_VALUE, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = ANIMATION_VALUE, easing = FastOutLinearInEasing)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(DIMEN_XX_NORMAL))
                .border(
                    width = DIMEN_XXX_SMALL,
                    color = color,
                    shape = RoundedCornerShape(DIMEN_XX_NORMAL)
                )
                .height(DIMEN_BUTTON)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = color10, shape = RoundedCornerShape(DIMEN_XX_NORMAL)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(DIMEN_XX_NORMAL))
                Image(
                    imageVector = ImageVector.vectorResource(id = image),
                    String.empty(),
                    alignment = Alignment.Center
                )
                Spacer(modifier = Modifier.width(DIMEN_XX_NORMAL))
                Text(
                    text = message,
                    modifier = Modifier.weight(1f),
                    color = color,
                    maxLines = 5
                )
            }
        }
    }
}