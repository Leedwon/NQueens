package com.ledwon.jakub.nqueens.features.game

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@Stable
@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val boardSize = savedStateHandle.toRoute<GameDestination>().boardSize

    private val _state = MutableStateFlow(createInitialState())
    val state = _state.asStateFlow()

    private fun createInitialState(): GameState {
        return GameState(
            boardSize = boardSize,
            cells = persistentMapOf()
        )
    }
}
