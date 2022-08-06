package com.thinkup.mvi.flows.changepassword

import com.thinkup.mvi.R
import com.thinkup.mvi.repositories.UserRepository
import com.thinkup.mvi.state.BaseViewModel
import com.thinkup.mvi.ui.shared.ANIMATION_VALUE
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.mvi.utils.ResourcesHelper
import com.thinkup.common.empty
import com.thinkup.common.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val repository: UserRepository,
    private val resourcesHelper: ResourcesHelper
) : BaseViewModel<ChangePasswordState, ChangePasswordUiEvent>() {

    private val reducer = ChangePasswordReducer(ChangePasswordState())

    override val state: StateFlow<ChangePasswordState>
        get() = reducer.state

    fun onHideAlert() {
        executeService(service = {
            val success = reducer.state.value.alertMessage?.isSuccess ?: false
            reducer.sendEvent(ChangePasswordUiEvent.HideAlertMessage)
            delay(ANIMATION_VALUE.toLong())
            if (success) reducer.sendEvent(ChangePasswordUiEvent.GoBack)
        })
    }

    fun onChangePassword(password: String) {
        reducer.sendEvent(ChangePasswordUiEvent.OnChangeCurrent(password))
    }

    fun onChangeNew(new: String) {
        reducer.sendEvent(ChangePasswordUiEvent.OnChangeNew(new))
    }

    fun onChangeRepeat(repeat: String) {
        reducer.sendEvent(ChangePasswordUiEvent.OnChangeRepeat(repeat))
    }

    private fun validateNewPassword(): Boolean = state.value.newPassword.isValidPassword()

    private fun validateRepeatPassword(): Boolean = state.value.repeatPassword.isValidPassword()

    private fun validateData(): Boolean {
        val hasPasswordError = !validateNewPassword()
        val hasRepeatError = !validateRepeatPassword() || (state.value.newPassword != state.value.repeatPassword)
        val hasError = hasRepeatError || hasPasswordError
        if (hasError) {
            reducer.sendEvent(
                ChangePasswordUiEvent.OnDataError(
                    alertMessage = AlertMessage(
                        message = resourcesHelper.getString(R.string.app_error),
                        isSuccess = false
                    ),
                    isPasswordError = if (hasPasswordError) resourcesHelper.getString(R.string.invalid_password) else String.empty(),
                    isRepeatError = if (hasRepeatError) resourcesHelper.getString(R.string.invalid_repeat_password) else String.empty()
                )
            )
        }
        return !hasError
    }

    fun changePassword() {
        executeService(
            service = {
                reducer.sendEvent(ChangePasswordUiEvent.Loading)
                if (validateData()) {
                    val oldPassword = state.value.password.orEmpty()
                    val newPassword = state.value.newPassword.orEmpty()
                    repository.changePassword(oldPassword, newPassword)
                    delay(1000)
                    reducer.sendEvent(
                        ChangePasswordUiEvent.OnSuccess(
                            alertMessage = AlertMessage(
                                message = resourcesHelper.getString(R.string.change_password_success),
                                isSuccess = true
                            )
                        )
                    )
                }
            }, onErrorCallback = {
                reducer.sendEvent(
                    ChangePasswordUiEvent.OnDataError(
                        alertMessage = AlertMessage(
                            message = resourcesHelper.getString(R.string.app_error),
                            isSuccess = false
                        )
                    )
                )
            }
        )
    }
}