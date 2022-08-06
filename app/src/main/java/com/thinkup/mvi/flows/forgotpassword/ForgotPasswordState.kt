package com.thinkup.mvi.flows.forgotpassword

import androidx.compose.runtime.Immutable
import com.thinkup.mvi.state.Reducer
import com.thinkup.mvi.state.UiEvent
import com.thinkup.mvi.state.UiState
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.common.empty

@Immutable
sealed class ForgotPasswordUiEvent : UiEvent {
    object Loading : ForgotPasswordUiEvent()
    data class OnChangeEmail(val email: String) : ForgotPasswordUiEvent()
    data class OnDataError(
        val alertMessage: AlertMessage? = null,
        val isEmailError: String = String.empty()
    ) : ForgotPasswordUiEvent()

    object HideAlertMessage : ForgotPasswordUiEvent()
    object GoBack : ForgotPasswordUiEvent()
    data class OnSuccess(val alertMessage: AlertMessage? = null) : ForgotPasswordUiEvent()
}

@Immutable
data class ForgotPasswordState(
    val showLoading: Boolean = false,
    val email: String = String.empty(),
    val alertMessage: AlertMessage? = null,
    val isEmailError: String = String.empty(),
    val goBack: Boolean = false
) : UiState

class ForgotPasswordReducer(initial: ForgotPasswordState) : Reducer<ForgotPasswordState, ForgotPasswordUiEvent>(initial) {
    override fun reduce(oldState: ForgotPasswordState, event: ForgotPasswordUiEvent) {
        when (event) {
            is ForgotPasswordUiEvent.GoBack -> {
                setState(oldState.copy(showLoading = false, goBack = true))
            }
            is ForgotPasswordUiEvent.HideAlertMessage -> {
                setState(oldState.copy(showLoading = false, alertMessage = null))
            }
            is ForgotPasswordUiEvent.OnChangeEmail -> {
                setState(oldState.copy(email = event.email))
            }
            is ForgotPasswordUiEvent.OnDataError -> {
                setState(
                    oldState.copy(
                        showLoading = false,
                        alertMessage = event.alertMessage,
                        isEmailError = event.isEmailError
                    )
                )
            }
            is ForgotPasswordUiEvent.OnSuccess -> {
                setState(oldState.copy(showLoading = false, alertMessage = event.alertMessage))
            }
            ForgotPasswordUiEvent.Loading -> {
                setState(oldState.copy(showLoading = true))
            }
        }
    }
}
