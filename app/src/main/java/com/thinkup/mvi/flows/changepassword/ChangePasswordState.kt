package com.thinkup.mvi.flows.changepassword

import androidx.compose.runtime.Immutable
import com.thinkup.mvi.state.Reducer
import com.thinkup.mvi.state.UiEvent
import com.thinkup.mvi.state.UiState
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.common.empty

@Immutable
sealed class ChangePasswordUiEvent : UiEvent {
    object Loading : ChangePasswordUiEvent()
    object HideAlertMessage : ChangePasswordUiEvent()
    data class OnChangeCurrent(val current: String) : ChangePasswordUiEvent()
    data class OnChangeNew(val new: String) : ChangePasswordUiEvent()
    data class OnChangeRepeat(val repeat: String) : ChangePasswordUiEvent()
    data class OnDataError(
        val alertMessage: AlertMessage,
        val isPasswordError: String = String.empty(),
        val isRepeatError: String = String.empty()
    ) : ChangePasswordUiEvent()

    object GoBack : ChangePasswordUiEvent()
    data class OnSuccess(val alertMessage: AlertMessage? = null) : ChangePasswordUiEvent()
}

@Immutable
data class ChangePasswordState(
    val showLoading: Boolean = false,
    val password: String? = null,
    val newPassword: String? = null,
    val repeatPassword: String? = null,
    val alertMessage: AlertMessage? = null,
    val isPasswordError: String = String.empty(),
    val isRepeatError: String = String.empty(),
    val goBack: Boolean = false
) : UiState

class ChangePasswordReducer(initial: ChangePasswordState) : Reducer<ChangePasswordState, ChangePasswordUiEvent>(initial) {
    override fun reduce(oldState: ChangePasswordState, event: ChangePasswordUiEvent) {
        when (event) {
            is ChangePasswordUiEvent.Loading -> {
                setState(oldState.copy(showLoading = true))
            }
            is ChangePasswordUiEvent.HideAlertMessage -> {
                setState(oldState.copy(showLoading = false, alertMessage = null))
            }
            is ChangePasswordUiEvent.OnChangeCurrent -> {
                setState(oldState.copy(showLoading = false, password = event.current))
            }
            is ChangePasswordUiEvent.OnChangeNew -> {
                setState(oldState.copy(showLoading = false, newPassword = event.new))
            }
            is ChangePasswordUiEvent.OnChangeRepeat -> {
                setState(oldState.copy(showLoading = false, repeatPassword = event.repeat))
            }
            is ChangePasswordUiEvent.OnDataError -> {
                setState(
                    oldState.copy(
                        showLoading = false,
                        alertMessage = event.alertMessage,
                        isPasswordError = event.isPasswordError,
                        isRepeatError = event.isRepeatError
                    )
                )
            }
            is ChangePasswordUiEvent.GoBack -> {
                setState(oldState.copy(showLoading = false, goBack = true))
            }
            is ChangePasswordUiEvent.OnSuccess -> {
                setState(oldState.copy(showLoading = false, alertMessage = event.alertMessage))
            }
        }
    }
}