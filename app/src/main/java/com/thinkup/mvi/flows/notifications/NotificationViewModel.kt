package com.thinkup.mvi.flows.notifications

import com.thinkup.models.app.Notification
import com.thinkup.mvi.repositories.NotificationRepository
import com.thinkup.mvi.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : BaseViewModel<NotificationState, NotificationUiEvent>() {

    init {
        getNotifications()
    }

    private val reducer = NotificationReducer(NotificationState())

    override val state: StateFlow<NotificationState>
        get() = reducer.state

    fun getNotifications() {
        executeService(service = {
            reducer.sendEvent(NotificationUiEvent.OnLoading)
            notificationRepository.getNotifications()?.let {
                reducer.sendEvent(NotificationUiEvent.OnSuccess(it))
            }
        })
    }

    fun deleteNotification(notification: Notification) {
        executeService(service = {
            reducer.sendEvent(NotificationUiEvent.OnLoading)
            notificationRepository.deleteNotification(notification)
            getNotifications()
        })
    }
}