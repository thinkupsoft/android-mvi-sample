package com.thinkup.mvi.flows.login

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.thinkup.mvi.R
import com.thinkup.mvi.repositories.AuthRepository
import com.thinkup.mvi.repositories.NotificationRepository
import com.thinkup.mvi.state.BaseViewModel
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
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val notificationRepository: NotificationRepository,
    private val resourcesHelper: ResourcesHelper
) : BaseViewModel<LoginScreenState, LoginScreenUiEvent>() {

    private val reducer = LoginReducer(LoginScreenState())

    override val state: StateFlow<LoginScreenState>
        get() = reducer.state

    fun onChangeEmail(email: String) {
        reducer.sendEvent(LoginScreenUiEvent.OnChangeEmail(email))
    }

    fun onChangePassword(password: String) {
        reducer.sendEvent(LoginScreenUiEvent.OnChangePassword(password))
    }

    fun onHideAlert() {
        executeService(service = {
            val success = reducer.state.value.alertMessage?.isSuccess ?: false
            reducer.sendEvent(LoginScreenUiEvent.HideAlertMessage)
            delay(ANIMATION_VALUE.toLong())
            if (success) reducer.sendEvent(LoginScreenUiEvent.GoBack)
        })
    }

    private fun validateEmail(): Boolean = state.value.email.isEmail()

    private fun validatePassword(): Boolean = state.value.password.isValidPassword()

    private fun validateData(): Boolean {
        val hasEmailError = !validateEmail()
        val hasPasswordError = !validatePassword()
        val hasError = hasEmailError || hasPasswordError
        if (hasError) {
            reducer.sendEvent(
                LoginScreenUiEvent.OnDataError(
                    alertMessage = AlertMessage(
                        message = resourcesHelper.getString(R.string.login_fail),
                        isSuccess = false
                    ),
                    isPasswordError = if (hasPasswordError) resourcesHelper.getString(R.string.invalid_password) else String.empty(),
                    isEmailError = if (hasEmailError) resourcesHelper.getString(R.string.invalid_email) else String.empty()
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

    fun login() {
        executeService(
            service = {
                reducer.sendEvent(LoginScreenUiEvent.OnClickLogin)
                if (validateData()) {
                    repository.login(
                        state.value.email.orEmpty(),
                        state.value.password.orEmpty()
                    )
                    saveFcmToken()
                    reducer.sendEvent(
                        LoginScreenUiEvent.OnLoginSuccess(
                            alertMessage = AlertMessage(
                                message = resourcesHelper.getString(R.string.login_success),
                                isSuccess = true
                            )
                        )
                    )
                }
            },
            onErrorCallback = {
                reducer.sendEvent(
                    LoginScreenUiEvent.OnDataError(
                        alertMessage = AlertMessage(
                            message = resourcesHelper.getString(R.string.login_fail),
                            isSuccess = false
                        )
                    )
                )
            }
        )
    }
}