package com.thinkup.mvi.flows.register

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.thinkup.mvi.R
import com.thinkup.mvi.repositories.AuthRepository
import com.thinkup.mvi.repositories.NotificationRepository
import com.thinkup.mvi.state.BaseViewModel
import com.thinkup.mvi.ui.EMAIL_IN_USE
import com.thinkup.mvi.ui.shared.ANIMATION_VALUE
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.mvi.utils.ResourcesHelper
import com.thinkup.common.empty
import com.thinkup.common.isEmail
import com.thinkup.common.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val notificationRepository: NotificationRepository,
    private val resourcesHelper: ResourcesHelper
) : BaseViewModel<RegisterScreenState, RegisterScreenUiEvent>() {

    private val reducer = RegisterReducer(RegisterScreenState())

    override val state: StateFlow<RegisterScreenState>
        get() = reducer.state

    fun onChangeName(name: String) {
        reducer.sendEvent(RegisterScreenUiEvent.OnChangeName(name))
    }

    fun onChangeEmail(email: String) {
        reducer.sendEvent(RegisterScreenUiEvent.OnChangeEmail(email))
    }

    fun onChangePassword(password: String) {
        reducer.sendEvent(RegisterScreenUiEvent.OnChangePassword(password))
    }

    fun onChangeRepeatPassword(repeatPassword: String) {
        reducer.sendEvent(RegisterScreenUiEvent.OnChangeRepeatPassword(repeatPassword))
    }

    fun onHideAlert() {
        executeService(service = {
            val success = reducer.state.value.alertMessage?.isSuccess ?: false
            reducer.sendEvent(RegisterScreenUiEvent.HideAlertMessage)
            delay(ANIMATION_VALUE.toLong())
            if (success) reducer.sendEvent(RegisterScreenUiEvent.GoBack)
        })
    }

    private fun validateEmail(): Boolean = state.value.email.isEmail()

    private fun validatePassword(): Boolean = state.value.password.isValidPassword()

    private fun validateRepeatPassword(): Boolean = state.value.password == state.value.repeatPassword

    private fun validateData(): Boolean {
        val hasNameError = state.value.name?.trim().isNullOrEmpty()
        val hasEmailError = !validateEmail()
        val hasPasswordError = !validatePassword()
        val hasRepeatError = !validateRepeatPassword()
        val hasError = hasEmailError || hasPasswordError || hasNameError || hasRepeatError
        if (hasError) {
            reducer.sendEvent(
                RegisterScreenUiEvent.OnDataError(
                    alertMessage = AlertMessage(
                        message = resourcesHelper.getString(R.string.login_fail),
                        isSuccess = false
                    ),
                    isNameError = if (hasNameError) resourcesHelper.getString(R.string.invalid_name) else String.empty(),
                    isEmailError = if (hasEmailError) resourcesHelper.getString(R.string.invalid_email) else String.empty(),
                    isPasswordError = if (hasPasswordError) resourcesHelper.getString(R.string.invalid_password) else String.empty(),
                    isRepeatPasswordError = if (hasRepeatError) resourcesHelper.getString(R.string.invalid_repeat_password) else String.empty()
                )
            )
        }
        return !hasError
    }

    private fun saveFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) return@OnCompleteListener
            // Get new FCM registration token
            val token = task.result
            executeService(service = {
                notificationRepository.addDevice(token)
            })
        })
    }

    fun register() {
        executeService(
            service = {
                reducer.sendEvent(RegisterScreenUiEvent.OnClickRegister)
                if (validateData()) {
                    repository.register(
                        state.value.name.orEmpty(),
                        state.value.email.orEmpty(),
                        state.value.password.orEmpty()
                    )
                    saveFcmToken()
                    reducer.sendEvent(
                        RegisterScreenUiEvent.OnRegisterSuccess(
                            alertMessage = AlertMessage(
                                message = resourcesHelper.getString(R.string.register_success),
                                isSuccess = true
                            )
                        )
                    )
                }
            },
            onErrorCallback = {
                val isEmailInUse = it?.msg == EMAIL_IN_USE || it?.errors?.errors?.map { it.msg }?.contains(EMAIL_IN_USE) == true
                reducer.sendEvent(
                    RegisterScreenUiEvent.OnDataError(
                        alertMessage = AlertMessage(
                            message = resourcesHelper.getString(R.string.register_fail),
                            isSuccess = false
                        ),
                        isEmailInUse = isEmailInUse
                    )
                )
            }
        )
    }
}