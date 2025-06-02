package com.ledwon.jakub.nqueens.features.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledwon.jakub.nqueens.R
import com.ledwon.jakub.nqueens.ui.components.ChessBoard
import com.ledwon.jakub.nqueens.ui.theme.NQueensTheme
import kotlinx.collections.immutable.persistentMapOf

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel()
) {
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    BackHandler {
        // TODO handle back press
    }
    GameContent(
        state = state,
        onBackClick = { TODO() }
    )
}

@Composable
private fun GameContent(
    state: GameState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { TopBar(onBackClick = onBackClick) }
    ) {
        ChessBoard(
            size = state.boardSize,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        )
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

@Preview
@Composable
private fun GameScreenPreview() {
    NQueensTheme {
        GameContent(
            state = GameState(
                boardSize = 8,
                cells = persistentMapOf()
            ),
            onBackClick = {}
        )
    }
}
