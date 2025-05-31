package com.ledwon.jakub.nqueens.features.mainmenu

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _boardSize = savedStateHandle.getStateFlow(key = BOARD_SIZE_KEY, initialValue = INITIAL_BOARD_SIZE)
    val state: StateFlow<MainMenuState> = _boardSize
        .map { MainMenuState(boardSize = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = initialState
        )

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun onBoardSizeChange(size: Int) {
        savedStateHandle[BOARD_SIZE_KEY] = size
    }

    fun onPlayClick() {
        viewModelScope.launch {
            _uiEffect.emit(UiEffect.NavigateToGame(state.value.boardSize))
        }
    }

    sealed interface UiEffect {
        data class NavigateToGame(val boardSize: Int) : UiEffect
    }

    private companion object {
        const val BOARD_SIZE_KEY = "board_size"
        const val INITIAL_BOARD_SIZE = 8
        val initialState = MainMenuState(boardSize = INITIAL_BOARD_SIZE)
    }
}
