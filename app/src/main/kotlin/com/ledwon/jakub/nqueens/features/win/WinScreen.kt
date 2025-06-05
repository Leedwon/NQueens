package com.ledwon.jakub.nqueens.features.win

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ledwon.jakub.nqueens.R
import com.ledwon.jakub.nqueens.ui.theme.NQueensTheme

@Composable
fun WinScreen(
    elapsedMillis: Long,
    navigateToMainMenu: () -> Unit
) {
    WinScreenContent(
        elapsedMillis = elapsedMillis,
        onMainMenuClick = navigateToMainMenu
    )
}

@Composable
private fun WinScreenContent(
    elapsedMillis: Long,
    onMainMenuClick: () -> Unit
) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(onMainMenuClick = onMainMenuClick) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            YouWon()
            YourTime(elapsedMillis = elapsedMillis)
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
private fun YourTime(elapsedMillis: Long) {
    Text(
        text = stringResource(R.string.your_time, formatMillis(elapsedMillis)),
        style = MaterialTheme.typography.bodyLarge,
    )
}

@ReadOnlyComposable
@Composable
private fun formatMillis(millis: Long): String {
    val minutes = (millis / 1000) / 60
    val seconds = (millis / 1000) % 60
    val ms = millis % 1000
    return String.format(
        LocalConfiguration.current.locales[0],
        "%02d:%02d.%03d",
        minutes,
        seconds,
        ms
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
            elapsedMillis = 123456,
            onMainMenuClick = {}
        )
    }
}
