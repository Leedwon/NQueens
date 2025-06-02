package com.ledwon.jakub.nqueens.features.game

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Stable
@HiltViewModel(assistedFactory = GameViewModel.Factory::class)
class GameViewModel @AssistedInject constructor(
    @Assisted private val boardSize: Int
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(boardSize: Int): GameViewModel
    }

    private val _state = MutableStateFlow(createInitialState())
    val state = _state.asStateFlow()

    private fun createInitialState(): GameState {
        return GameState(
            boardSize = boardSize,
            cells = persistentMapOf()
        )
    }
}
