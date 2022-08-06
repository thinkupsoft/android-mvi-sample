package com.thinkup.mvi.flows.home

import androidx.compose.runtime.Immutable
import com.thinkup.mvi.state.Reducer
import com.thinkup.mvi.state.UiEvent
import com.thinkup.mvi.state.UiState
import com.thinkup.models.app.Movie

@Immutable
sealed class HomeScreenUiEvent : UiEvent {
    object LoadingFM : HomeScreenUiEvent()
    object LoadingMM : HomeScreenUiEvent()
    data class NeedLogin(val needLogin: Boolean = true) : HomeScreenUiEvent()
    data class ClearLogin(val needLogin: Boolean = false) : HomeScreenUiEvent()
    data class OnFeaturedMovies(val featuredMovies: List<Movie>) : HomeScreenUiEvent()
    data class OnMyMovies(val myMovies: List<Movie>) : HomeScreenUiEvent()
    object OnReload : HomeScreenUiEvent()
}

@Immutable
data class HomeScreenState(
    val isLoadingFM: Boolean = true,
    val isLoadingMM: Boolean = true,
    val featuredMovies: List<Movie> = emptyList(),
    val myMovies: List<Movie> = emptyList(),
    val needLogin: Boolean = false,
    val reload: Boolean = false
) : UiState

class HomeReducer(initial: HomeScreenState) : Reducer<HomeScreenState, HomeScreenUiEvent>(initial) {
    override fun reduce(oldState: HomeScreenState, event: HomeScreenUiEvent) {
        when (event) {
            is HomeScreenUiEvent.NeedLogin -> {
                setState(oldState.copy(needLogin = event.needLogin, reload = false))
            }
            is HomeScreenUiEvent.ClearLogin -> {
                setState(oldState.copy(needLogin = event.needLogin, reload = false))
            }
            HomeScreenUiEvent.OnReload -> {
                setState(oldState.copy(reload = true))
            }
            is HomeScreenUiEvent.LoadingFM -> {
                setState(oldState.copy(isLoadingFM = true))
            }
            is HomeScreenUiEvent.LoadingMM -> {
                setState(oldState.copy(isLoadingMM = true))
            }
            is HomeScreenUiEvent.OnFeaturedMovies -> {
                setState(oldState.copy(isLoadingFM = false, featuredMovies = event.featuredMovies))
            }
            is HomeScreenUiEvent.OnMyMovies -> {
                setState(oldState.copy(isLoadingMM = false, myMovies = event.myMovies))
            }
        }
    }
}