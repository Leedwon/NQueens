package com.ledwon.jakub.nqueens.features.game

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentMap

class GameStatePreviewParameterProvider : PreviewParameterProvider<GameState> {

    override val values: Sequence<GameState> = sequenceOf(
        GameState(
            boardSize = 4,
            cells = fullBoard(4, emptyMap())
        ),

        GameState(
            boardSize = 4,
            cells = fullBoard(
                4, mapOf(
                    BoardPosition(1, 1) to Cell(hasQueen = true, hasConflict = false)
                )
            )
        ),

        GameState(
            boardSize = 4,
            cells = fullBoard(
                4, mapOf(
                    BoardPosition(0, 0) to Cell(hasQueen = true, hasConflict = true),
                    BoardPosition(0, 1) to Cell(hasQueen = true, hasConflict = true),
                    BoardPosition(0, 2) to Cell(hasQueen = false, hasConflict = true),
                    BoardPosition(0, 3) to Cell(hasQueen = false, hasConflict = true),
                    BoardPosition(3, 2) to Cell(hasQueen = true, hasConflict = false),
                )
            )
        ),

        GameState(
            boardSize = 4,
            cells = fullBoard(
                4, mapOf(
                    BoardPosition(0, 1) to Cell(hasQueen = true, hasConflict = false),
                    BoardPosition(1, 3) to Cell(hasQueen = true, hasConflict = false),
                    BoardPosition(2, 0) to Cell(hasQueen = true, hasConflict = false),
                    BoardPosition(3, 2) to Cell(hasQueen = true, hasConflict = false),
                )
            )
        )
    )
}

private fun fullBoard(
    boardSize: Int,
    overrides: Map<BoardPosition, Cell>
): PersistentMap<BoardPosition, Cell> {
    val board = mutableMapOf<BoardPosition, Cell>()

    for (row in 0 until boardSize) {
        for (col in 0 until boardSize) {
            val pos = BoardPosition(row, col)
            board[pos] = overrides[pos] ?: Cell(hasQueen = false, hasConflict = false)
        }
    }

    return board.toPersistentMap()
}

