package com.ledwon.jakub.nqueens.features.game

import com.ledwon.jakub.nqueens.core.game.Conflicts
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentMap
import javax.inject.Inject
import com.ledwon.jakub.nqueens.core.game.BoardPosition as CoreBoardPosition
import com.ledwon.jakub.nqueens.core.game.GameState as CoreGameState

class GameStateFactory @Inject constructor() {

    fun createState(
        boardSize: Int,
        gameState: CoreGameState,
        elapsedMillis: Long
    ): GameState {
        return GameState(
            boardSize = boardSize,
            cells = createCells(
                boardSize = boardSize,
                queens = gameState.queens,
                conflicts = gameState.conflicts
            ),
            queensMetadata = createQueensMetadata(
                boardSize = boardSize,
                queens = gameState.queens,
                conflicts = gameState.conflicts
            ),
            elapsedMillis = elapsedMillis
        )
    }

    private fun createCells(
        boardSize: Int,
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

    private fun createQueensMetadata(
        boardSize: Int,
        queens: Set<CoreBoardPosition>,
        conflicts: Conflicts
    ): QueensMetadata {
        val correctlyPlaced = queens.count { it !in conflicts }
        val conflicting = queens.size - correctlyPlaced
        return QueensMetadata(
            goal = boardSize,
            correctlyPlaced = correctlyPlaced,
            conflicting = conflicting
        )
    }

    private fun CoreBoardPosition.toUiPosition(): BoardPosition =
        BoardPosition(row = row, column = column)
}
