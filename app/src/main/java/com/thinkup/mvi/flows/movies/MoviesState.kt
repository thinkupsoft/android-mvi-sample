package com.thinkup.mvi.flows.movies

import androidx.compose.runtime.Immutable
import com.thinkup.mvi.state.Reducer
import com.thinkup.mvi.state.UiEvent
import com.thinkup.mvi.state.UiState

@Immutable
sealed class MoviesUiEvent : UiEvent {
    object OnNoInternet : MoviesUiEvent()
}

@Immutable
data class MoviesState(
    val noInternetClicked: Int = 0
) : UiState

class MoviesReducer(initial: MoviesState) : Reducer<MoviesState, MoviesUiEvent>(initial) {
    override fun reduce(oldState: MoviesState, event: MoviesUiEvent) {
        when (event) {
            MoviesUiEvent.OnNoInternet -> {
                setState(oldState.copy(noInternetClicked = oldState.noInternetClicked + 1))
            }
        }
    }
}
