package com.thinkup.mvi.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface UiState
interface UiEvent

abstract class Reducer<State : UiState, Event : UiEvent>(initialVal: State) {

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialVal)
    val state: StateFlow<State>
        get() = _state

    val stateHandler: StateHandler<State> = StateHandlerImpl { storedState ->
        _state.tryEmit(storedState)
    }

    init {
        stateHandler.addState(initialVal)
    }

    fun sendEvent(event: Event) {
        reduce(_state.value, event)
    }

    fun setState(newState: State) {
        val success = _state.tryEmit(newState)
        if (success) {
            stateHandler.addState(newState)
        }
    }

    abstract fun reduce(oldState: State, event: Event)
}

interface StateHandler<State : UiState> {
    fun addState(state: State)
    fun selectState(position: Int)
    fun getStates(): List<State>
}

class StateHandlerImpl<State : UiState>(
    private val onStateSelected: (State) -> Unit
) : StateHandler<State> {

    private val states = mutableListOf<State>()

    override fun addState(state: State) {
        states.add(state)
    }

    override fun selectState(position: Int) {
        onStateSelected(states[position])
    }

    override fun getStates(): List<State> {
        return states
    }
}