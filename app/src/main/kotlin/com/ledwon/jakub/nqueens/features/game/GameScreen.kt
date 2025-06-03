package com.ledwon.jakub.nqueens.features.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledwon.jakub.nqueens.R
import com.ledwon.jakub.nqueens.ui.components.BoardCell
import com.ledwon.jakub.nqueens.ui.components.BoardCellColors
import com.ledwon.jakub.nqueens.ui.components.BoardCellDefaults
import com.ledwon.jakub.nqueens.ui.components.ChessBoard
import com.ledwon.jakub.nqueens.ui.components.ChessBoardScope
import com.ledwon.jakub.nqueens.ui.theme.NQueensTheme
import kotlinx.collections.immutable.ImmutableMap

@Composable
fun GameScreen(
    boardSize: Int,
    gameViewModel: GameViewModel = hiltViewModel<GameViewModel, GameViewModel.Factory> { factory ->
        factory.create(boardSize = boardSize)
    }
) {
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    BackHandler {
        // TODO handle back press
    }
    GameContent(
        state = state,
        onCellClick = { gameViewModel.onCellClick(it) },
        onBackClick = { TODO() }
    )
}

@Composable
private fun GameContent(
    state: GameState,
    onCellClick: (BoardPosition) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { TopBar(onBackClick = onBackClick) }
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QueensInfo(
                modifier = Modifier.padding(horizontal = 16.dp),
                queensMetadata = state.queensMetadata
            )
            Board(
                cells = state.cells,
                size = state.boardSize,
                onCellClick = onCellClick,
            )
        }
    }
}

@Composable
private fun TopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.n_queens)) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun Board(
    size: Int,
    cells: ImmutableMap<BoardPosition, Cell>,
    onCellClick: (BoardPosition) -> Unit,
    modifier: Modifier = Modifier
) {
    ChessBoard(
        size = size,
        modifier = modifier,
        cell = {
            val boardPosition = BoardPosition(row = row, column = column)
            val cell = cells[boardPosition] ?: error("Cell not found for position: $boardPosition")
            Cell(
                cell = cell,
                onClick = { onCellClick(boardPosition) }
            )
        }
    )
}

@Composable
private fun ChessBoardScope.Cell(
    cell: Cell,
    onClick: () -> Unit
) {
    BoardCell(
        colors = getCellColors(cell),
        modifier = Modifier.clickable(onClick = onClick),
        showLabels = true
    ) {
        Queen(
            visible = cell.hasQueen,
            modifier = Modifier.size(cellSize / 2)
        )
    }
}

@Composable
private fun Queen(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_queen),
            contentDescription = null,
        )
    }

}

@Composable
private fun QueensInfo(
    queensMetadata: QueensMetadata,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        repeat(queensMetadata.goal) { index ->
            val color by animateColorAsState(
                when {
                    index < queensMetadata.correctlyPlaced -> MaterialTheme.colorScheme.primary
                    index < queensMetadata.correctlyPlaced + queensMetadata.conflicting -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            )
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.ic_queen),
                contentDescription = null,
                tint = color
            )
        }
    }

}

@Composable
private fun getCellColors(cell: Cell): BoardCellColors {
    val defaultColors = BoardCellDefaults.boardCellColors()
    val whiteCellColor by animateColorAsState(if (cell.hasConflict) MaterialTheme.colorScheme.error else defaultColors.whiteCellColor)
    val blackCellColor by animateColorAsState(if (cell.hasConflict) MaterialTheme.colorScheme.errorContainer else defaultColors.blackCellColor)

    return BoardCellColors(
        whiteCellColor = whiteCellColor,
        blackCellColor = blackCellColor,
    )
}

@Preview
@Composable
private fun GameScreenPreview(
    @PreviewParameter(GameStatePreviewParameterProvider::class) state: GameState
) {
    NQueensTheme {
        GameContent(
            state = state,
            onCellClick = {},
            onBackClick = {}
        )
    }
}
