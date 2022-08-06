package com.thinkup.mvi.flows.home

import com.thinkup.mvi.repositories.AuthRepository
import com.thinkup.mvi.repositories.MovieRepository
import com.thinkup.mvi.repositories.NotificationRepository
import com.thinkup.mvi.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val movieRepository: MovieRepository,
    private val notificationRepository: NotificationRepository
) : BaseViewModel<HomeScreenState, HomeScreenUiEvent>() {

    private val reducer = HomeReducer(HomeScreenState())

    override val state: StateFlow<HomeScreenState>
        get() = reducer.state

    init {
        getFeaturedMovies()
    }

    fun isUserLogged() = repository.isUserLogged()

    fun onNeedLogin() = reducer.sendEvent(HomeScreenUiEvent.NeedLogin())

    fun onClearNeedLogin() = reducer.sendEvent(HomeScreenUiEvent.ClearLogin())

    fun onReload() = reducer.sendEvent(HomeScreenUiEvent.OnReload)

    fun loadMyMovies() {
        if (isUserLogged() && state.value.myMovies.isEmpty()) getMyMovies()
    }

    private fun getFeaturedMovies() {
        executeService(service = {
            reducer.sendEvent(HomeScreenUiEvent.LoadingFM)
            val movies = movieRepository.getFeaturedMovies()
            reducer.sendEvent(HomeScreenUiEvent.OnFeaturedMovies(movies.data))
        }, onErrorCallback = {
            reducer.sendEvent(HomeScreenUiEvent.OnFeaturedMovies(emptyList()))
        })
    }

    private fun getMyMovies() {
        executeService(service = {
            reducer.sendEvent(HomeScreenUiEvent.LoadingMM)
            val movies = movieRepository.getMyMovies(0, 5)
            reducer.sendEvent(HomeScreenUiEvent.OnMyMovies(movies.data))
        }, onErrorCallback = {
            reducer.sendEvent(HomeScreenUiEvent.OnMyMovies(emptyList()))
        })
    }

    fun logout() {
        executeService(service = {
            notificationRepository.removeDevice()
            repository.logout()
        })
    }
}