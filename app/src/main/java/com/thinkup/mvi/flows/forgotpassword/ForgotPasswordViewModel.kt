package com.thinkup.mvi.flows.forgotpassword

import com.thinkup.mvi.R
import com.thinkup.mvi.repositories.AuthRepository
import com.thinkup.mvi.state.BaseViewModel
import com.thinkup.mvi.ui.shared.ANIMATION_VALUE
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.mvi.utils.ResourcesHelper
import com.thinkup.common.empty
import com.thinkup.common.isEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val resourcesHelper: ResourcesHelper
) : BaseViewModel<ForgotPasswordState, ForgotPasswordUiEvent>() {

    private val reducer = ForgotPasswordReducer(ForgotPasswordState())

    override val state: StateFlow<ForgotPasswordState>
        get() = reducer.state

    fun onChangeEmail(email: String) {
        reducer.sendEvent(ForgotPasswordUiEvent.OnChangeEmail(email))
    }

    fun onHideAlert() {
        executeService(service = {
            val success = reducer.state.value.alertMessage?.isSuccess ?: false
            reducer.sendEvent(ForgotPasswordUiEvent.HideAlertMessage)
            delay(ANIMATION_VALUE.toLong())
            if (success) reducer.sendEvent(ForgotPasswordUiEvent.GoBack)
        })
    }

    private fun validateEmail(): Boolean = state.value.email.isEmail()

    private fun validateData(): Boolean {
        val hasEmailError = !validateEmail()
        if (hasEmailError) {
            reducer.sendEvent(
                ForgotPasswordUiEvent.OnDataError(
                    alertMessage = AlertMessage(
                        message = resourcesHelper.getString(R.string.app_error),
                        isSuccess = false
                    ),
                    isEmailError = if (hasEmailError) resourcesHelper.getString(R.string.invalid_email) else String.empty()
                )
            )
        }
        return !hasEmailError
    }

    fun forgotPassword() {
        executeService(service = {
            reducer.sendEvent(ForgotPasswordUiEvent.Loading)
            if (validateData()) {
                repository.forgotPassword(state.value.email)
                reducer.sendEvent(
                    ForgotPasswordUiEvent.OnSuccess(
                        alertMessage = AlertMessage(
                            message = resourcesHelper.getString(R.string.forgot_pass_success_message),
                            isSuccess = true
                        )
                    )
                )
            }
        }, onErrorCallback = {
            reducer.sendEvent(
                ForgotPasswordUiEvent.OnDataError(
                    alertMessage = AlertMessage(
                        message = resourcesHelper.getString(R.string.app_error),
                        isSuccess = false
                    )
                )
            )
        })
    }
}