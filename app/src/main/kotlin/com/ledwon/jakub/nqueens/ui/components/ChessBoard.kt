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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.ledwon.jakub.nqueens.R
import com.ledwon.jakub.nqueens.ui.theme.NQueensTheme

data class ChessBoardScope(
    val boardSize: Int,
    val row: Int,
    val column: Int,
    val cellSize: Dp
) {
    fun isFirstColumn() = column == 0
    fun isLastRow() = row == boardSize - 1
}

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
                        Box(
                            Modifier
                                .size(cellSize)
                                .testTag(ChessBoardTestTags.createCellTag(row = row, column = col))
                        ) {
                            ChessBoardScope(
                                boardSize = size,
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
    @ReadOnlyComposable
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
    modifier: Modifier = Modifier,
    colors: BoardCellColors = BoardCellDefaults.boardCellColors(),
    showLabels: Boolean = false,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
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
        if (showLabels && isFirstColumn()) {
            CellLabel(
                text = (boardSize - row).toString(),
                modifier = Modifier.align(Alignment.TopStart)
            )
        }
        if (showLabels && isLastRow()) {
            CellLabel(
                text = getNotationForFile(column).toString(),
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
private fun ChessBoardScope.CellLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = with(LocalDensity.current) { (cellSize / 4).toSp() },
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both
            )
        ),
        text = text
    )
}

private fun getNotationForFile(column: Int): Char {
    return 'a' + column
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
private fun ChessBoardWithLabelsPreview() {
    NQueensTheme {
        ChessBoard(
            size = 8,
            modifier = Modifier.size(256.dp),
            cell = {
                BoardCell(showLabels = true)
            }
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
                BoardCell(showLabels = true) {
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
