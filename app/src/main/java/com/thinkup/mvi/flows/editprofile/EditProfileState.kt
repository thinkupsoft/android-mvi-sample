package com.thinkup.mvi.flows.editprofile

import androidx.compose.runtime.Immutable
import com.thinkup.mvi.state.Reducer
import com.thinkup.mvi.state.UiEvent
import com.thinkup.mvi.state.UiState
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.common.empty

@Immutable
sealed class EditProfileScreenUiEvent : UiEvent {
    object Loading : EditProfileScreenUiEvent()
    data class OnLoad(val name: String, val username: String) : EditProfileScreenUiEvent()
    object OnClickEdit : EditProfileScreenUiEvent()
    data class OnChangeName(val name: String) : EditProfileScreenUiEvent()
    data class OnChangeUsername(val username: String) : EditProfileScreenUiEvent()
    object HideAlertMessage : EditProfileScreenUiEvent()
    object GoBack : EditProfileScreenUiEvent()
    data class OnDataError(
        val alertMessage: AlertMessage? = null,
        val isNameError: String = String.empty(),
        val isUsernameError: String = String.empty()
    ) : EditProfileScreenUiEvent()

    data class OnEditSuccess(val alertMessage: AlertMessage? = null) : EditProfileScreenUiEvent()
}

@Immutable
data class EditProfileScreenState(
    val showLoading: Boolean = false,
    val name: String? = null,
    val username: String? = null,
    val isNameError: String = String.empty(),
    val isUsernameError: String = String.empty(),
    val isUsernameInUse: Boolean? = null,
    val alertMessage: AlertMessage? = null,
    val goBack: Boolean = false
) : UiState

class EditProfileReducer(initial: EditProfileScreenState) : Reducer<EditProfileScreenState, EditProfileScreenUiEvent>(initial) {
    override fun reduce(oldState: EditProfileScreenState, event: EditProfileScreenUiEvent) {
        when (event) {
            is EditProfileScreenUiEvent.Loading -> {
                setState(oldState.copy(showLoading = true))
            }
            is EditProfileScreenUiEvent.OnLoad -> {
                setState(oldState.copy(showLoading = false, name = event.name, username = event.username))
            }
            is EditProfileScreenUiEvent.GoBack -> {
                setState(oldState.copy(goBack = true))
            }
            is EditProfileScreenUiEvent.HideAlertMessage -> {
                setState(oldState.copy(showLoading = false, alertMessage = null))
            }
            is EditProfileScreenUiEvent.OnChangeName -> {
                setState(oldState.copy(name = event.name))
            }
            is EditProfileScreenUiEvent.OnChangeUsername -> {
                setState(oldState.copy(username = event.username))
            }
            is EditProfileScreenUiEvent.OnClickEdit -> {
                setState(
                    oldState.copy(
                        showLoading = true,
                        isNameError = String.empty(),
                        isUsernameError = String.empty()
                    )
                )
            }
            is EditProfileScreenUiEvent.OnDataError -> {
                setState(
                    oldState.copy(
                        showLoading = false,
                        alertMessage = event.alertMessage,
                        isNameError = event.isNameError,
                        isUsernameError = event.isUsernameError
                    )
                )
            }
            is EditProfileScreenUiEvent.OnEditSuccess -> {
                setState(
                    oldState.copy(
                        alertMessage = event.alertMessage,
                        showLoading = false,
                        isNameError = String.empty(),
                        isUsernameError = String.empty(),
                        isUsernameInUse = null
                    )
                )
            }
        }
    }
}