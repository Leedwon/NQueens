package com.ledwon.jakub.nqueens.features.mainmenu

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Stable
class MainMenuViewModel : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun onBoardSizeChange(size: Int) {
        _state.update { it.copy(boardSize = size) }
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
        val initialState = MainMenuState(boardSize = 8)
    }
}
