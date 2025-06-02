package com.ledwon.jakub.nqueens.features.game

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledwon.jakub.nqueens.core.game.Conflicts
import com.ledwon.jakub.nqueens.core.game.GameEngineFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import com.ledwon.jakub.nqueens.core.game.BoardPosition as CoreBoardPosition

@Stable
@HiltViewModel(assistedFactory = GameViewModel.Factory::class)
class GameViewModel @AssistedInject constructor(
    @Assisted private val boardSize: Int,
    gameEngineFactory: GameEngineFactory
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(boardSize: Int): GameViewModel
    }

    private val gameEngine = gameEngineFactory.create(boardSize = boardSize)

    val state = gameEngine.state.map {
        GameState(
            boardSize = boardSize,
            cells = createCells(
                queens = it.queens,
                conflicts = it.conflicts
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = createInitialState()
    )

    fun onCellClick(position: BoardPosition) {
        gameEngine.toggleQueen(position = position.toCorePosition())
    }

    private fun createCells(
        queens: Set<CoreBoardPosition>,
        conflicts: Conflicts
    ): ImmutableMap<BoardPosition, Cell> {
        return (0 until boardSize).flatMap { row ->
            (0 until boardSize).map { column ->
                val position = CoreBoardPosition(row, column)
                val cell = Cell(
                    hasQueen = position in queens,
                    hasConflict = position in conflicts
                )
                position.toUiPosition() to cell
            }
        }
            .toMap()
            .toPersistentMap()
    }

    private fun createInitialState(): GameState {
        return GameState(
            boardSize = boardSize,
            cells = createCells(
                queens = emptySet(),
                conflicts = Conflicts(
                    rows = emptySet(),
                    columns = emptySet(),
                    majorDiagonals = emptySet(),
                    minorDiagonals = emptySet()
                )
            )
        )
    }

    private fun CoreBoardPosition.toUiPosition(): BoardPosition =
        BoardPosition(row = row, column = column)

    private fun BoardPosition.toCorePosition(): CoreBoardPosition =
        CoreBoardPosition(row = row, column = column)
}
