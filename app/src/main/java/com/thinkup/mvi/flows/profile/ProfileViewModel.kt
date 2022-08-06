package com.thinkup.mvi.flows.profile

import android.graphics.Bitmap
import android.net.Uri
import com.thinkup.mvi.R
import com.thinkup.mvi.repositories.AuthRepository
import com.thinkup.mvi.repositories.NotificationRepository
import com.thinkup.mvi.repositories.UserRepository
import com.thinkup.mvi.state.BaseViewModel
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.mvi.utils.ResourcesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
    private val resourcesHelper: ResourcesHelper
) : BaseViewModel<ProfileScreenState, ProfileScreenUiEvent>() {

    private val reducer: ProfileReducer = ProfileReducer(ProfileScreenState())
    override val state: StateFlow<ProfileScreenState>
        get() = reducer.state

    init {
        load()
    }

    fun load() {
        reducer.sendEvent(ProfileScreenUiEvent.Loading())
        executeService(service = {
            val user = repository.getCurrentUser()
            reducer.sendEvent(ProfileScreenUiEvent.ShowData(user = user))
        })
    }

    fun logout() {
        reducer.sendEvent(ProfileScreenUiEvent.Loading())
        executeService(service = {
            notificationRepository.removeDevice()
            repository.logout()
            reducer.sendEvent(ProfileScreenUiEvent.OnLogout())
        })
    }

    fun updateAvatar(bitmap: Bitmap? = null, uri: Uri? = null) {
        reducer.sendEvent(ProfileScreenUiEvent.Loading())
        executeService(service = {
            val file = bitmap?.let {
                resourcesHelper.saveBitmap(resourcesHelper.getFileTempDir().path, it)
            } ?: run {
                uri?.let {
                    resourcesHelper.getFile(it)
                }
            }
            file?.let {
                val user = userRepository.updateAvatar(it)
                reducer.sendEvent(
                    ProfileScreenUiEvent.OnSuccess(
                        alertMessage = AlertMessage(
                            resourcesHelper.getString(R.string.profile_success_msg),
                            true
                        )
                    )
                )
                reducer.sendEvent(ProfileScreenUiEvent.ShowData(user = user))
            } ?: reducer.sendEvent(ProfileScreenUiEvent.OnError(alertMessage = AlertMessage(resourcesHelper.getString(R.string.app_error), false)))
        }, onErrorCallback = {
            reducer.sendEvent(ProfileScreenUiEvent.OnError(alertMessage = AlertMessage(it?.getMessage().orEmpty(), false)))
        })
    }

    fun onHideAlert() {
        executeService(service = {
            reducer.sendEvent(ProfileScreenUiEvent.HideAlertMessage)
        })
    }

}