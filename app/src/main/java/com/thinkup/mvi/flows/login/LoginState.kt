package com.thinkup.mvi.flows.login

import androidx.compose.runtime.Immutable
import com.thinkup.mvi.state.Reducer
import com.thinkup.mvi.state.UiEvent
import com.thinkup.mvi.state.UiState
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.common.empty

@Immutable
sealed class LoginScreenUiEvent : UiEvent {
    object OnClickLogin : LoginScreenUiEvent()
    data class OnChangeEmail(val email: String) : LoginScreenUiEvent()
    data class OnChangePassword(val password: String) : LoginScreenUiEvent()
    data class OnDataError(
        val alertMessage: AlertMessage? = null,
        val isEmailError: String = String.empty(),
        val isPasswordError: String = String.empty()
    ) : LoginScreenUiEvent()

    object HideAlertMessage : LoginScreenUiEvent()
    object GoBack : LoginScreenUiEvent()
    data class OnLoginSuccess(val alertMessage: AlertMessage? = null) : LoginScreenUiEvent()
}

@Immutable
data class LoginScreenState(
    val showLoading: Boolean = false,
    val email: String? = null,
    val password: String? = null,
    val alertMessage: AlertMessage? = null,
    val isEmailError: String = String.empty(),
    val isPasswordError: String = String.empty(),
    val goBack: Boolean = false
) : UiState

class LoginReducer(initial: LoginScreenState) : Reducer<LoginScreenState, LoginScreenUiEvent>(initial) {
    override fun reduce(oldState: LoginScreenState, event: LoginScreenUiEvent) {
        when (event) {
            is LoginScreenUiEvent.OnClickLogin -> {
                setState(
                    oldState.copy(
                        showLoading = true,
                        isEmailError = String.empty(),
                        isPasswordError = String.empty()
                    )
                )
            }
            is LoginScreenUiEvent.OnChangeEmail -> {
                setState(oldState.copy(email = event.email))
            }
            is LoginScreenUiEvent.OnChangePassword -> {
                setState(oldState.copy(password = event.password))
            }
            is LoginScreenUiEvent.OnDataError -> {
                setState(
                    oldState.copy(
                        showLoading = false,
                        alertMessage = event.alertMessage,
                        isEmailError = event.isEmailError,
                        isPasswordError = event.isPasswordError
                    )
                )
            }
            is LoginScreenUiEvent.OnLoginSuccess -> {
                setState(
                    oldState.copy(
                        alertMessage = event.alertMessage,
                        showLoading = false,
                        isEmailError = String.empty(),
                        isPasswordError = String.empty()
                    )
                )
            }
            is LoginScreenUiEvent.HideAlertMessage -> {
                setState(oldState.copy(showLoading = false, alertMessage = null))
            }
            is LoginScreenUiEvent.GoBack -> {
                setState(oldState.copy(goBack = true))
            }
        }
    }
}