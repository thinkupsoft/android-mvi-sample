package com.thinkup.mvi.flows.deleteaccount

import androidx.compose.runtime.Immutable
import com.thinkup.mvi.state.Reducer
import com.thinkup.mvi.state.UiEvent
import com.thinkup.mvi.state.UiState
import com.thinkup.mvi.ui.shared.AlertMessage

@Immutable
sealed class DeleteAccountScreenUiEvent : UiEvent {
    object Loading : DeleteAccountScreenUiEvent()
    object OnDeleteSuccess : DeleteAccountScreenUiEvent()
    object HideAlertMessage : DeleteAccountScreenUiEvent()
    data class OnDeleteError(val alertMessage: AlertMessage) : DeleteAccountScreenUiEvent()
}

@Immutable
data class DeleteAccountScreenState(
    val showLoading: Boolean = false,
    val alertMessage: AlertMessage? = null,
    val goBack: Boolean = false,
    val isSuccess: Boolean? = null
) : UiState

class DeleteAccountReducer(initial: DeleteAccountScreenState) : Reducer<DeleteAccountScreenState, DeleteAccountScreenUiEvent>(initial) {
    override fun reduce(oldState: DeleteAccountScreenState, event: DeleteAccountScreenUiEvent) {
        when (event) {
            is DeleteAccountScreenUiEvent.Loading -> {
                setState(oldState.copy(showLoading = true))
            }
            is DeleteAccountScreenUiEvent.OnDeleteSuccess -> {
                setState(oldState.copy(showLoading = false, isSuccess = true, goBack = true))
            }
            is DeleteAccountScreenUiEvent.HideAlertMessage -> {
                setState(oldState.copy(showLoading = false, alertMessage = null))
            }
            is DeleteAccountScreenUiEvent.OnDeleteError -> {
                setState(
                    oldState.copy(
                        showLoading = false,
                        isSuccess = false,
                        alertMessage = event.alertMessage,
                        goBack = false
                    )
                )
            }
        }
    }
}