package com.ledwon.jakub.nqueens.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.ledwon.jakub.nqueens.R
import com.ledwon.jakub.nqueens.ui.theme.NQueensTheme

data class ChessBoardScope(
    val row: Int,
    val column: Int,
    val cellSize: Dp
)

@Composable
fun ChessBoard(
    size: Int,
    modifier: Modifier = Modifier,
    cell: @Composable ChessBoardScope.() -> Unit = { BoardCell() }
) {
    BoxWithConstraints(modifier) {
        val constraint = min(this.maxWidth, this.maxHeight)
        val cellSize = constraint / size
        Column {
            repeat(size) { row ->
                Row(Modifier.fillMaxWidth()) {
                    repeat(size) { col ->
                        Box(Modifier.size(cellSize)) {
                            ChessBoardScope(
                                row = row,
                                column = col,
                                cellSize = cellSize
                            ).cell()
                        }
                    }
                }
            }
        }
    }
}

@Immutable
data class BoardCellColors(
    val whiteCellColor: Color,
    val blackCellColor: Color,
)

data object BoardCellDefaults {
    @Composable
    fun boardCellColors(
        whiteCellColor: Color = MaterialTheme.colorScheme.primary,
        blackCellColor: Color = MaterialTheme.colorScheme.onPrimary
    ): BoardCellColors = BoardCellColors(
        whiteCellColor = whiteCellColor,
        blackCellColor = blackCellColor
    )
}

@Composable
fun ChessBoardScope.BoardCell(
    colors: BoardCellColors = BoardCellDefaults.boardCellColors(),
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(cellSize)
            .background(
                if ((row + column) % 2 == 0) {
                    colors.blackCellColor
                } else {
                    colors.whiteCellColor
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
@Preview
private fun ChessBoardPreview() {
    NQueensTheme {
        ChessBoard(
            size = 8,
            modifier = Modifier.size(256.dp)
        )
    }
}

@Composable
@Preview
private fun ChessBoardWithPiecesPreview() {
    NQueensTheme {
        ChessBoard(
            size = 8,
            modifier = Modifier.size(256.dp),
            cell = {
                BoardCell {
                    Icon(
                        modifier = Modifier.size(cellSize / 2),
                        painter = painterResource(R.drawable.ic_queen),
                        contentDescription = null
                    )
                }
            }
        )
    }
}
