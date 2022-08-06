package com.thinkup.mvi.flows.forgotpassword

import com.thinkup.mvi.R
import com.thinkup.mvi.repositories.AuthRepository
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
class RedeemPasswordViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val resourcesHelper: ResourcesHelper
) : BaseViewModel<RedeemState, RedeemUiEvent>() {

    private val reducer = RedeemReducer(RedeemState())

    override val state: StateFlow<RedeemState>
        get() = reducer.state

    fun onHideAlert() {
        executeService(service = {
            val success = reducer.state.value.alertMessage?.isSuccess ?: false
            reducer.sendEvent(RedeemUiEvent.HideAlertMessage)
            delay(ANIMATION_VALUE.toLong())
            if (success) reducer.sendEvent(RedeemUiEvent.GoBack)
        })
    }

    fun onChangeNew(new: String) {
        reducer.sendEvent(RedeemUiEvent.OnChangeNew(new))
    }

    fun onChangeRepeat(repeat: String) {
        reducer.sendEvent(RedeemUiEvent.OnChangeRepeat(repeat))
    }

    private fun validateNewPassword(): Boolean = state.value.newPassword.isValidPassword()

    private fun validateRepeatPassword(): Boolean = state.value.repeatPassword.isValidPassword()

    private fun validateData(): Boolean {
        val hasPasswordError = !validateNewPassword()
        val hasRepeatError = !validateRepeatPassword() || (state.value.newPassword != state.value.repeatPassword)
        val hasError = hasRepeatError || hasPasswordError
        if (hasError) {
            reducer.sendEvent(
                RedeemUiEvent.OnDataError(
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

    fun redeemPassword(token: String) {
        executeService(
            service = {
                reducer.sendEvent(RedeemUiEvent.Loading)
                if (validateData()) {
                    val newPassword = state.value.newPassword.orEmpty()
                    repository.redeemPassword(token, newPassword)
                    reducer.sendEvent(
                        RedeemUiEvent.OnSuccess(
                            alertMessage = AlertMessage(
                                message = resourcesHelper.getString(R.string.change_password_success),
                                isSuccess = true
                            )
                        )
                    )
                }
            }, onErrorCallback = {
                reducer.sendEvent(
                    RedeemUiEvent.OnDataError(
                        alertMessage = AlertMessage(
                            message = resourcesHelper.getString(R.string.redeem_pass_error_message),
                            isSuccess = false
                        )
                    )
                )
            }
        )
    }
}