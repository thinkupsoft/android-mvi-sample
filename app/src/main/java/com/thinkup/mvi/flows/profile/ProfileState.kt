package com.thinkup.mvi.flows.profile

import androidx.compose.runtime.Immutable
import com.thinkup.mvi.state.Reducer
import com.thinkup.mvi.state.UiEvent
import com.thinkup.mvi.state.UiState
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.models.app.User

@Immutable
sealed class ProfileScreenUiEvent : UiEvent {
    data class Loading(val showLoading: Boolean = true) : ProfileScreenUiEvent()
    data class ShowData(val user: User? = null, val showLoading: Boolean = false) : ProfileScreenUiEvent()
    data class OnLogout(val logoutCompleted: Boolean = true) : ProfileScreenUiEvent()
    data class OnError(val alertMessage: AlertMessage) : ProfileScreenUiEvent()
    data class OnSuccess(val alertMessage: AlertMessage) : ProfileScreenUiEvent()
    object HideAlertMessage : ProfileScreenUiEvent()
}

@Immutable
data class ProfileScreenState(
    val showLoading: Boolean = false,
    val logoutCompleted: Boolean = false,
    val user: User? = null,
    val alertMessage: AlertMessage? = null
) : UiState

class ProfileReducer(initial: ProfileScreenState) : Reducer<ProfileScreenState, ProfileScreenUiEvent>(initial) {
    override fun reduce(oldState: ProfileScreenState, event: ProfileScreenUiEvent) {
        when (event) {
            is ProfileScreenUiEvent.Loading -> {
                setState(oldState.copy(showLoading = event.showLoading, user = null))
            }
            is ProfileScreenUiEvent.ShowData -> {
                setState(oldState.copy(showLoading = event.showLoading, user = event.user))
            }
            is ProfileScreenUiEvent.OnLogout -> {
                setState(oldState.copy(logoutCompleted = event.logoutCompleted, user = null))
            }
            is ProfileScreenUiEvent.OnError -> {
                setState(oldState.copy(showLoading = false, alertMessage = event.alertMessage))
            }
            is ProfileScreenUiEvent.OnSuccess -> {
                setState(oldState.copy(showLoading = false, alertMessage = event.alertMessage))
            }
            is ProfileScreenUiEvent.HideAlertMessage -> {
                setState(oldState.copy(showLoading = false, alertMessage = null))
            }
        }
    }
}