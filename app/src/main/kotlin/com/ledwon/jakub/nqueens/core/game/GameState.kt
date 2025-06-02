package com.ledwon.jakub.nqueens.core.game

data class BoardPosition(
    val row: Int,
    val column: Int
) {
    val majorDiagonal: Int
        get() = row - column
    val minorDiagonal: Int
        get() = row + column
}

data class Conflicts(
    val rows: Set<Int>,
    val columns: Set<Int>,
    val majorDiagonals: Set<Int>,
    val minorDiagonals: Set<Int>
) {
    operator fun contains(position: BoardPosition): Boolean {
        return position.row in rows || position.column in columns ||
                position.majorDiagonal in majorDiagonals || position.minorDiagonal in minorDiagonals
    }
}

data class GameState(
    val queens: Set<BoardPosition>,
    val conflicts: Conflicts,
    val hasWon: Boolean
)
