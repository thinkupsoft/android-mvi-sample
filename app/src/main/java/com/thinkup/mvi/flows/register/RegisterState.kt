package com.thinkup.mvi.flows.register

import androidx.compose.runtime.Immutable
import com.thinkup.mvi.state.Reducer
import com.thinkup.mvi.state.UiEvent
import com.thinkup.mvi.state.UiState
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.common.empty

@Immutable
sealed class RegisterScreenUiEvent : UiEvent {
    object OnClickRegister : RegisterScreenUiEvent()
    data class OnChangeName(val name: String) : RegisterScreenUiEvent()
    data class OnChangeEmail(val email: String) : RegisterScreenUiEvent()
    data class OnChangePassword(val password: String) : RegisterScreenUiEvent()
    data class OnChangeRepeatPassword(val repeatPassword: String) : RegisterScreenUiEvent()
    data class OnDataError(
        val alertMessage: AlertMessage? = null,
        val isNameError: String = String.empty(),
        val isEmailError: String = String.empty(),
        val isPasswordError: String = String.empty(),
        val isRepeatPasswordError: String = String.empty(),
        val isEmailInUse: Boolean? = null
    ) : RegisterScreenUiEvent()

    object HideAlertMessage : RegisterScreenUiEvent()
    object GoBack : RegisterScreenUiEvent()
    data class OnRegisterSuccess(val alertMessage: AlertMessage? = null) : RegisterScreenUiEvent()
}

@Immutable
data class RegisterScreenState(
    val showLoading: Boolean = false,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val repeatPassword: String? = null,
    val alertMessage: AlertMessage? = null,
    val isNameError: String = String.empty(),
    val isEmailError: String = String.empty(),
    val isPasswordError: String = String.empty(),
    val isRepeatPasswordError: String = String.empty(),
    val goBack: Boolean = false,
    val isEmailInUse: Boolean? = null
) : UiState

class RegisterReducer(initial: RegisterScreenState) : Reducer<RegisterScreenState, RegisterScreenUiEvent>(initial) {
    override fun reduce(oldState: RegisterScreenState, event: RegisterScreenUiEvent) {
        when (event) {
            is RegisterScreenUiEvent.OnClickRegister -> {
                setState(
                    oldState.copy(
                        showLoading = true,
                        isEmailError = String.empty(),
                        isPasswordError = String.empty()
                    )
                )
            }
            is RegisterScreenUiEvent.OnChangeName -> {
                setState(oldState.copy(name = event.name))
            }
            is RegisterScreenUiEvent.OnChangeEmail -> {
                setState(oldState.copy(email = event.email))
            }
            is RegisterScreenUiEvent.OnChangePassword -> {
                setState(oldState.copy(password = event.password))
            }
            is RegisterScreenUiEvent.OnChangeRepeatPassword -> {
                setState(oldState.copy(repeatPassword = event.repeatPassword))
            }
            is RegisterScreenUiEvent.OnDataError -> {
                setState(
                    oldState.copy(
                        showLoading = false,
                        alertMessage = event.alertMessage,
                        isNameError = event.isNameError,
                        isEmailError = event.isEmailError,
                        isPasswordError = event.isPasswordError,
                        isRepeatPasswordError = event.isRepeatPasswordError,
                        isEmailInUse = event.isEmailInUse
                    )
                )
            }
            is RegisterScreenUiEvent.OnRegisterSuccess -> {
                setState(
                    oldState.copy(
                        alertMessage = event.alertMessage,
                        showLoading = false,
                        isEmailError = String.empty(),
                        isPasswordError = String.empty(),
                        isEmailInUse = false
                    )
                )
            }
            is RegisterScreenUiEvent.HideAlertMessage -> {
                setState(oldState.copy(showLoading = false, alertMessage = null))
            }
            is RegisterScreenUiEvent.GoBack -> {
                setState(oldState.copy(goBack = true))
            }
        }
    }
}