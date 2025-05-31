package com.ledwon.jakub.nqueens.features.mainmenu

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledwon.jakub.nqueens.R
import com.ledwon.jakub.nqueens.features.mainmenu.MainMenuViewModel.UiEffect.NavigateToGame
import com.ledwon.jakub.nqueens.ui.components.ChessBoard
import com.ledwon.jakub.nqueens.ui.theme.NQueensTheme

@Composable
fun MainMenuScreen(
    viewModel: MainMenuViewModel = hiltViewModel(),
    navigateToGame: (boardSize: Int) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect {
            when (it) {
                is NavigateToGame -> navigateToGame(it.boardSize)
            }
        }
    }

    MainMenuContent(
        state = state,
        onBoardSizeChange = { viewModel.onBoardSizeChange(it) },
        onPlayClick = { viewModel.onPlayClick() }
    )
}

@Composable
private fun MainMenuContent(
    state: MainMenuState,
    onBoardSizeChange: (Int) -> Unit,
    onPlayClick: () -> Unit
) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(onPlayClick = onPlayClick) }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            item(key = "board_size_picker") {
                BoardSizePicker(
                    boardSize = state.boardSize,
                    onBoardSizeChange = onBoardSizeChange
                )
            }
        }
    }
}

@Composable
private fun TopBar() {
    TopAppBar(
        title = { Text(stringResource(R.string.n_queens)) }
    )
}

@Composable
private fun BoardSizePicker(
    boardSize: Int,
    onBoardSizeChange: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BoardSizeTitle()
        BoardSizeSlider(
            value = boardSize.toFloat(),
            onValueChange = { onBoardSizeChange(it.toInt()) },
        )
        BoardPreview(boardSize = boardSize)

    }
}

@Composable
private fun BoardSizeTitle() {
    Text(text = stringResource(R.string.board_size))
}

@Composable
private fun BoardSizeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    boardSizes: ClosedFloatingPointRange<Float> = AVAILABLE_BOARD_SIZES
) {
    val steps = boardSizes.endInclusive - boardSizes.start - 1

    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = boardSizes,
        steps = steps.toInt()
    )
}

@Composable
private fun BoardPreview(boardSize: Int) {
    val animatedSize by animateDpAsState(
        targetValue = ((boardSize / 20f) * 256).dp
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ChessBoard(
            modifier = Modifier
                .size(animatedSize)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.surfaceVariant),
            size = boardSize.toInt()
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.board_size_template, boardSize),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun BottomBar(
    onPlayClick: () -> Unit
) {
    BottomAppBar {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = onPlayClick,
            content = {
                Text(stringResource(R.string.play))
            }
        )
    }
}

private val AVAILABLE_BOARD_SIZES = 4f..20f

@Preview
@Composable
private fun MainMenuScreenPreview() {
    NQueensTheme {
        MainMenuContent(
            state = MainMenuState(boardSize = 8),
            onBoardSizeChange = {},
            onPlayClick = {}
        )
    }
}
