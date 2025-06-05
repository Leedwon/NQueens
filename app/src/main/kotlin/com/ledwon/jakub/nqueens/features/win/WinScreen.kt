package com.ledwon.jakub.nqueens.features.win

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ledwon.jakub.nqueens.R
import com.ledwon.jakub.nqueens.ui.theme.NQueensTheme

@Composable
fun WinScreen(
    navigateToMainMenu: () -> Unit
) {
    WinScreenContent(
        onMainMenuClick = navigateToMainMenu
    )
}

@Composable
private fun WinScreenContent(
    onMainMenuClick: () -> Unit
) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(onMainMenuClick = onMainMenuClick) }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            YouWon()
        }
    }
}

@Composable
private fun TopBar() {
    TopAppBar(
        title = { Text(stringResource(R.string.congratulations)) },
    )
}

@Composable
private fun YouWon() {
    Text(
        text = stringResource(R.string.you_won),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun BottomBar(
    onMainMenuClick: () -> Unit
) {
    BottomAppBar {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = onMainMenuClick
        ) {
            Text(stringResource(R.string.main_menu))
        }
    }
}

@Preview
@Composable
private fun WinScreenPreview() {
    NQueensTheme {
        WinScreenContent(
            onMainMenuClick = {}
        )
    }
}
