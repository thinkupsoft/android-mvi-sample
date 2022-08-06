package com.thinkup.mvi.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thinkup.common.services.ApiError
import com.thinkup.common.services.Errors
import com.thinkup.common.services.ServiceError
import com.thinkup.common.services.ServiceException
import com.thinkup.common.services.SessionException
import com.thinkup.common.services.UnavailableException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState, in Event : UiEvent> : ViewModel() {

    abstract val state: StateFlow<State>

    /**
     * Coroutine to execute a block in a new Thread
     * @param service: Block to execute (DB, service, etc)
     */
    fun executeService(
        service: suspend () -> Unit,
        onErrorCallback: ((ServiceError?) -> Unit)? = null,
    ) {
        viewModelScope.launch {
            coroutineScope {
                try {
                    service()
                } catch (ex: ServiceException) {
                    ex.printStackTrace()
                    onErrorCallback?.invoke(ex.error)
                } catch (ex: SessionException) {
                    throw ex
                } catch (ex: UnavailableException) {
                    throw ex
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    onErrorCallback?.invoke(ServiceError("500", ex.message, Errors(listOf(ApiError(msg = ex.message.orEmpty())))))
                }
            }
        }
    }
}