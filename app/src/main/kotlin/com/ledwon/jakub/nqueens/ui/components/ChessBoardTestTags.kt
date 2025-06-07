package com.ledwon.jakub.nqueens.ui.components

object ChessBoardTestTags {

    fun createCellTag(row: Int, column: Int): String {
        return "chess_board_cell_${row}_$column"
    }
}
