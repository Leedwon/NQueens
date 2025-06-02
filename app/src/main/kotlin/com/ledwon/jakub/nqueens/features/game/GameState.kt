package com.ledwon.jakub.nqueens.features.game

import kotlinx.collections.immutable.ImmutableMap

data class GameState(
    val boardSize: Int,
    val cells: ImmutableMap<BoardPosition, Cell>
)

data class BoardPosition(
    val row: Int,
    val column: Int
)

data class Cell(
    val hasQueen: Boolean,
    val hasConflict: Boolean
)
