package com.thinkup.mvi.flows.deleteaccount

import com.thinkup.mvi.R
import com.thinkup.mvi.repositories.NotificationRepository
import com.thinkup.mvi.repositories.UserRepository
import com.thinkup.mvi.state.BaseViewModel
import com.thinkup.mvi.ui.shared.AlertMessage
import com.thinkup.mvi.utils.ResourcesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val repository: UserRepository,
    private val notificationRepository: NotificationRepository,
    private val resourcesHelper: ResourcesHelper
) : BaseViewModel<DeleteAccountScreenState, DeleteAccountScreenUiEvent>() {

    private val reducer = DeleteAccountReducer(DeleteAccountScreenState())

    override val state: StateFlow<DeleteAccountScreenState>
        get() = reducer.state

    fun onHideAlert() {
        executeService(service = {
            reducer.sendEvent(DeleteAccountScreenUiEvent.HideAlertMessage)
        })
    }

    fun delete() {
        reducer.sendEvent(DeleteAccountScreenUiEvent.Loading)
        executeService(service = {
            notificationRepository.removeDevice()
            repository.deleteAccount()
            reducer.sendEvent(DeleteAccountScreenUiEvent.OnDeleteSuccess)
        }, onErrorCallback = {
            reducer.sendEvent(
                DeleteAccountScreenUiEvent.OnDeleteError(
                    alertMessage = AlertMessage(
                        message = resourcesHelper.getString(R.string.app_error),
                        isSuccess = false
                    )
                )
            )
        })
    }
}