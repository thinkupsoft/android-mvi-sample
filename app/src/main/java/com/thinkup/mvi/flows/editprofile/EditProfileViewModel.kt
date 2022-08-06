package com.thinkup.mvi.flows.editprofile

import com.thinkup.mvi.R
import com.thinkup.mvi.repositories.AuthRepository
import com.thinkup.mvi.repositories.UserRepository
import com.thinkup.mvi.state.BaseViewModel
import com.thinkup.mvi.ui.USERNAME_IN_USE
import com.thinkup.mvi.ui.shared.ANIMATION_VALUE
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.mvi.utils.ResourcesHelper
import com.thinkup.common.empty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: UserRepository,
    private val authRepository: AuthRepository,
    private val resourcesHelper: ResourcesHelper
) : BaseViewModel<EditProfileScreenState, EditProfileScreenUiEvent>() {

    private val reducer = EditProfileReducer(EditProfileScreenState())

    override val state: StateFlow<EditProfileScreenState>
        get() = reducer.state

    init {
        executeService(service = {
            reducer.sendEvent(EditProfileScreenUiEvent.Loading)
            val user = authRepository.getCurrentUser()
            user?.let {
                reducer.sendEvent(EditProfileScreenUiEvent.OnLoad(user.name, user.username))
            }
        })
    }

    fun onChangeName(name: String) {
        reducer.sendEvent(EditProfileScreenUiEvent.OnChangeName(name))
    }

    fun onChangeUsername(username: String) {
        reducer.sendEvent(EditProfileScreenUiEvent.OnChangeUsername(username))
    }

    fun onHideAlert() {
        executeService(service = {
            val success = reducer.state.value.alertMessage?.isSuccess ?: false
            reducer.sendEvent(EditProfileScreenUiEvent.HideAlertMessage)
            delay(ANIMATION_VALUE.toLong())
            if (success) reducer.sendEvent(EditProfileScreenUiEvent.GoBack)
        })
    }

    private fun validateData(): Boolean {
        val hasNameError = state.value.name?.trim().isNullOrEmpty()
        val hasUsernameError = state.value.username?.trim().isNullOrEmpty()
        val hasError = hasUsernameError || hasNameError
        if (hasError) {
            reducer.sendEvent(
                EditProfileScreenUiEvent.OnDataError(
                    alertMessage = AlertMessage(
                        message = resourcesHelper.getString(R.string.app_error),
                        isSuccess = false
                    ),
                    isNameError = if (hasNameError) resourcesHelper.getString(R.string.invalid_name) else String.empty(),
                    isUsernameError = if (hasUsernameError) resourcesHelper.getString(R.string.invalid_username) else String.empty()
                )
            )
        }
        return !hasError
    }

    fun edit() {
        executeService(
            service = {
                reducer.sendEvent(EditProfileScreenUiEvent.OnClickEdit)
                if (validateData()) {
                    repository.updateProfile(
                        state.value.name.orEmpty(),
                        state.value.username.orEmpty()
                    )
                    reducer.sendEvent(
                        EditProfileScreenUiEvent.OnEditSuccess(
                            alertMessage = AlertMessage(
                                message = resourcesHelper.getString(R.string.edit_profile_success),
                                isSuccess = true
                            )
                        )
                    )
                }
            },
            onErrorCallback = {
                val isUsernameInUse = it?.msg == USERNAME_IN_USE || it?.errors?.errors?.map { it.msg }?.contains(USERNAME_IN_USE) == true
                reducer.sendEvent(
                    EditProfileScreenUiEvent.OnDataError(
                        alertMessage = AlertMessage(
                            message = resourcesHelper.getString(R.string.edit_profile_failed),
                            isSuccess = false
                        ),
                        isUsernameError = if (isUsernameInUse) resourcesHelper.getString(R.string.username_used) else String.empty()
                    )
                )
            }
        )
    }
}