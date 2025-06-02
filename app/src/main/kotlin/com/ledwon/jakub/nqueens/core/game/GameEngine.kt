package com.ledwon.jakub.nqueens.core.game

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameEngine @AssistedInject constructor(
    @Assisted private val boardSize: Int
) {

    private val _state = MutableStateFlow(createInitialState())
    val state = _state.asStateFlow()

    private val rowsWithQueens = mutableMapOf<Int, Int>()
    private val columnsWithQueens = mutableMapOf<Int, Int>()
    private val majorDiagonalsWithQueens = mutableMapOf<Int, Int>()
    private val minorDiagonalsWithQueens = mutableMapOf<Int, Int>()

    fun toggleQueen(position: BoardPosition) {
        updateLineCounts(position)
        updateState(position)
    }

    private fun updateLineCounts(queenPosition: BoardPosition) {
        val delta = if (state.value.queens.contains(queenPosition)) -1 else 1
        rowsWithQueens[queenPosition.row] =
            rowsWithQueens.getOrDefault(queenPosition.row, 0) + delta
        columnsWithQueens[queenPosition.column] =
            columnsWithQueens.getOrDefault(queenPosition.column, 0) + delta
        majorDiagonalsWithQueens[queenPosition.majorDiagonal] =
            majorDiagonalsWithQueens.getOrDefault(queenPosition.majorDiagonal, 0) + delta
        minorDiagonalsWithQueens[queenPosition.minorDiagonal] =
            minorDiagonalsWithQueens.getOrDefault(queenPosition.minorDiagonal, 0) + delta
    }

    private fun updateState(queenPosition: BoardPosition) {
        _state.update { currentState ->
            currentState
                .updateQueenAt(queenPosition)
                .updateConflicts()
                .calculateWinCondition()
        }
    }

    private fun GameState.updateQueenAt(position: BoardPosition): GameState {
        val newQueens = if (queens.contains(position)) {
            queens - position
        } else {
            queens + position
        }
        return copy(queens = newQueens)
    }

    private fun GameState.updateConflicts(): GameState {
        val rowConflicts = rowsWithQueens.filter { it.value > 1 }.keys
        val columnConflicts = columnsWithQueens.filter { it.value > 1 }.keys
        val majorDiagonalConflicts = majorDiagonalsWithQueens.filter { it.value > 1 }.keys
        val minorDiagonalConflicts = minorDiagonalsWithQueens.filter { it.value > 1 }.keys

        val conflicts = Conflicts(
            rows = rowConflicts,
            columns = columnConflicts,
            majorDiagonals = majorDiagonalConflicts,
            minorDiagonals = minorDiagonalConflicts
        )

        return copy(conflicts = conflicts)

    }

    private fun GameState.calculateWinCondition(): GameState {
        val hasWon = queens.size == boardSize && conflicts.areEmpty()
        return copy(hasWon = hasWon)
    }

    private fun Conflicts.areEmpty(): Boolean {
        return rows.isEmpty() && columns.isEmpty() && majorDiagonals.isEmpty() && minorDiagonals.isEmpty()
    }

    private fun createInitialState(): GameState {
        return GameState(
            queens = emptySet(),
            conflicts = Conflicts(
                rows = emptySet(),
                columns = emptySet(),
                majorDiagonals = emptySet(),
                minorDiagonals = emptySet()
            ),
            hasWon = false
        )
    }
}

@AssistedFactory
interface GameEngineFactory {
    fun create(boardSize: Int): GameEngine
}
