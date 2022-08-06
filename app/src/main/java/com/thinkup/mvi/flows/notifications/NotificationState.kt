package com.thinkup.mvi.flows.notifications

import androidx.compose.runtime.Immutable
import com.thinkup.models.app.Notification
import com.thinkup.mvi.state.Reducer
import com.thinkup.mvi.state.UiEvent
import com.thinkup.mvi.state.UiState

@Immutable
sealed class NotificationUiEvent : UiEvent {
    object OnLoading : NotificationUiEvent()
    data class OnSuccess(val notifications: List<Notification>) : NotificationUiEvent()
}

@Immutable
data class NotificationState(
    val showLoading: Boolean = true,
    val notifications: List<Notification> = emptyList()
) : UiState

class NotificationReducer(initial: NotificationState) : Reducer<NotificationState, NotificationUiEvent>(initial) {
    override fun reduce(oldState: NotificationState, event: NotificationUiEvent) {
        when (event) {
            NotificationUiEvent.OnLoading -> {
                setState(oldState.copy(showLoading = true))
            }
            is NotificationUiEvent.OnSuccess -> {
                setState(oldState.copy(showLoading = false, notifications = event.notifications))
            }
        }
    }
}