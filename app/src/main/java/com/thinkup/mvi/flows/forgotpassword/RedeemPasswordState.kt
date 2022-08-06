package com.thinkup.mvi.flows.forgotpassword

import androidx.compose.runtime.Immutable
import com.thinkup.mvi.state.Reducer
import com.thinkup.mvi.state.UiEvent
import com.thinkup.mvi.state.UiState
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.common.empty

@Immutable
sealed class RedeemUiEvent : UiEvent {
    object Loading : RedeemUiEvent()
    object HideAlertMessage : RedeemUiEvent()
    data class OnChangeNew(val new: String) : RedeemUiEvent()
    data class OnChangeRepeat(val repeat: String) : RedeemUiEvent()
    data class OnDataError(
        val alertMessage: AlertMessage,
        val isPasswordError: String = String.empty(),
        val isRepeatError: String = String.empty()
    ) : RedeemUiEvent()

    object GoBack : RedeemUiEvent()
    data class OnSuccess(val alertMessage: AlertMessage? = null) : RedeemUiEvent()
}

@Immutable
data class RedeemState(
    val showLoading: Boolean = false,
    val newPassword: String? = null,
    val repeatPassword: String? = null,
    val alertMessage: AlertMessage? = null,
    val isPasswordError: String = String.empty(),
    val isRepeatError: String = String.empty(),
    val goBack: Boolean = false
) : UiState

class RedeemReducer(initial: RedeemState) : Reducer<RedeemState, RedeemUiEvent>(initial) {
    override fun reduce(oldState: RedeemState, event: RedeemUiEvent) {
        when (event) {
            is RedeemUiEvent.Loading -> {
                setState(oldState.copy(showLoading = true))
            }
            is RedeemUiEvent.HideAlertMessage -> {
                setState(oldState.copy(showLoading = false, alertMessage = null))
            }
            is RedeemUiEvent.OnChangeNew -> {
                setState(oldState.copy(showLoading = false, newPassword = event.new))
            }
            is RedeemUiEvent.OnChangeRepeat -> {
                setState(oldState.copy(showLoading = false, repeatPassword = event.repeat))
            }
            is RedeemUiEvent.OnDataError -> {
                setState(
                    oldState.copy(
                        showLoading = false,
                        alertMessage = event.alertMessage,
                        isPasswordError = event.isPasswordError,
                        isRepeatError = event.isRepeatError
                    )
                )
            }
            is RedeemUiEvent.GoBack -> {
                setState(oldState.copy(showLoading = false, goBack = true))
            }
            is RedeemUiEvent.OnSuccess -> {
                setState(oldState.copy(showLoading = false, alertMessage = event.alertMessage))
            }
        }
    }
}