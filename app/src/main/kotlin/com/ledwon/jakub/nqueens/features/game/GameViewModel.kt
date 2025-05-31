package com.ledwon.jakub.nqueens.features.game

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@Stable
@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val gameDestination = savedStateHandle.toRoute<GameDestination>()

    private val _state = MutableStateFlow(GameState(boardSize = gameDestination.boardSize))
    val state = _state.asStateFlow()
}
