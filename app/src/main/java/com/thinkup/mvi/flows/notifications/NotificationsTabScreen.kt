package com.thinkup.mvi.flows.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.thinkup.mvi.R
import com.thinkup.mvi.ui.shared.TkupCustomLoader
import com.thinkup.mvi.ui.shared.TkupCustomLoaderConfig
import com.thinkup.mvi.ui.shared.TkupEmptyView
import com.thinkup.mvi.ui.shared.TkupEmptyViewConfig
import com.thinkup.mvi.ui.shared.Type
import com.thinkup.mvi.ui.theme.Background
import com.thinkup.mvi.ui.theme.DIMEN_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_MIN_SPACE
import com.thinkup.mvi.ui.theme.DIMEN_XX_NORMAL
import com.thinkup.mvi.ui.theme.DIMEN_DOUBLE_NORMAL
import com.thinkup.mvi.ui.theme.Error
import com.thinkup.mvi.ui.theme.Gray1
import com.thinkup.mvi.ui.theme.Typography
import com.thinkup.mvi.ui.theme.screenMessage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotificationsTabScreen(
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.getNotifications()
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    if (state.notifications.isNotEmpty()) {

        LazyColumn {
            items(state.notifications) {
                val dismissState = rememberDismissState()

                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                    coroutineScope.launch {
                        viewModel.deleteNotification(it)
                        dismissState.reset()
                    }
                }
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = { FractionalThreshold(0.5f) },
                    background = {
                        Box(
                            Modifier
                                .background(Error)
                                .fillMaxSize()
                                .padding(end = DIMEN_DOUBLE_NORMAL),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.tkup_delete_icon),
                                contentDescription = "Delete Icon",
                                tint = Color.White
                            )
                        }
                    },
                    dismissContent = {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(Background)
                        ) {
                            Text(
                                text = it.title, style = Typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = DIMEN_XX_NORMAL, vertical = DIMEN_NORMAL)
                            )
                            Text(
                                text = it.body, style = Typography.screenMessage,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = DIMEN_XX_NORMAL)
                                    .padding(bottom = DIMEN_NORMAL)
                            )
                            Divider(
                                modifier = Modifier.padding(horizontal = DIMEN_XX_NORMAL),
                                color = Gray1, thickness = DIMEN_MIN_SPACE
                            )
                        }
                    }
                )
            }
        }
    } else {
        NotificationsEmptyState()
    }

    TkupCustomLoader(
        TkupCustomLoaderConfig(
            visible = state.showLoading,
            type = Type.SOFT
        )
    )
}

@Composable
fun NotificationsEmptyState() {
    TkupEmptyView(
        config = TkupEmptyViewConfig(
            message = stringResource(id = R.string.notification_empty_message),
            icon = R.drawable.tkup_empty_notifications
        )
    )
}