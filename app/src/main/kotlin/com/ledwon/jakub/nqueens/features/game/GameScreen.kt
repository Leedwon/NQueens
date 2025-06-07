package com.ledwon.jakub.nqueens.features.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledwon.jakub.nqueens.R
import com.ledwon.jakub.nqueens.core.datetime.compose.formatMillis
import com.ledwon.jakub.nqueens.features.game.GameViewModel.UiEffect.NavigateToWinScreen
import com.ledwon.jakub.nqueens.ui.components.BoardCell
import com.ledwon.jakub.nqueens.ui.components.BoardCellColors
import com.ledwon.jakub.nqueens.ui.components.BoardCellDefaults
import com.ledwon.jakub.nqueens.ui.components.ChessBoard
import com.ledwon.jakub.nqueens.ui.components.ChessBoardScope
import com.ledwon.jakub.nqueens.ui.theme.NQueensTheme
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    boardSize: Int,
    navigateToWinScreen: (elapsedMillis: Long) -> Unit,
    navigateBack: () -> Unit,
    gameViewModel: GameViewModel = hiltViewModel<GameViewModel, GameViewModel.Factory> { factory ->
        factory.create(boardSize = boardSize)
    }
) {
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        gameViewModel.uiEffect.collect {
            when (it) {
                is NavigateToWinScreen -> navigateToWinScreen(it.elapsedMillis)
            }
        }
    }

    GameContent(
        state = state,
        onCellClick = { gameViewModel.onCellClick(it) },
        onRestartClick = { gameViewModel.onRestartClick() },
        onBackClick = navigateBack
    )
}

@Composable
private fun GameContent(
    state: GameState,
    onCellClick: (BoardPosition) -> Unit,
    onRestartClick: () -> Unit = {},
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { TopBar(onBackClick = onBackClick) }
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QueensInfo(
                    modifier = Modifier.weight(1f),
                    queensMetadata = state.queensMetadata
                )
                ElapsedTime(millis = state.elapsedMillis)
                RestartButton(onClick = onRestartClick)
            }
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
        modifier = Modifier
            .clickable(onClick = onClick)
            .semantics { hasConflict = cell.hasConflict },
        showLabels = true
    ) {
        Queen(
            visible = cell.hasQueen,
            modifier = Modifier
                .testTag(GameTestTags.QUEEN)
                .size(cellSize / 2)
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
        horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
        verticalArrangement = Arrangement.spacedBy(space = 4.dp)
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
private fun ElapsedTime(millis: Long) {
    Box(
        Modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = MaterialTheme.shapes.medium
        )
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = formatMillis(millis),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun RestartButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    IconButton(
        modifier = modifier,
        onClick = {
            onClick()
            coroutineScope.launch {
                rotation.snapTo(0f)
                rotation.animateTo(
                    targetValue = 360f,
                    animationSpec = tween(durationMillis = 500, easing = LinearEasing)
                )
            }
        }
    ) {
        Icon(
            modifier = Modifier.rotate(rotation.value),
            imageVector = Icons.Default.Refresh,
            contentDescription = null
        )
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
