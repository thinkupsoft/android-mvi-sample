package com.thinkup.mvi.flows.movies

import com.thinkup.mvi.di.DirectorsPagingVM
import com.thinkup.mvi.state.BaseViewModel
import com.thinkup.mvi.ui.shared.TkupPagingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DirectorsViewModel @Inject constructor(
    @DirectorsPagingVM val pagingViewModel: TkupPagingViewModel
) : BaseViewModel<MoviesState, MoviesUiEvent>() {

    private val reducer = MoviesReducer(MoviesState())

    override val state: StateFlow<MoviesState>
        get() = reducer.state

    fun onReload() = reducer.sendEvent(MoviesUiEvent.OnNoInternet)
}