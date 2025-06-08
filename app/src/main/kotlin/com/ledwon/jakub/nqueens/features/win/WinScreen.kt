package com.ledwon.jakub.nqueens.features.win

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ledwon.jakub.nqueens.R
import com.ledwon.jakub.nqueens.core.datetime.compose.formatMillis
import com.ledwon.jakub.nqueens.ui.theme.NQueensTheme
import kotlin.math.sin

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
        AnimatedQueensBox(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Column {
                YouWon()
                YourTime(elapsedMillis = elapsedMillis)
            }
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
        modifier = Modifier.testTag(WinTestTags.YOUR_TIME),
        text = stringResource(R.string.your_time, formatMillis(elapsedMillis)),
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun AnimatedQueensBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
    ) {
        QueensRow(time = time)
        content()
        QueensRow(time = time)
    }
}

@Composable
private fun QueensRow(
    time: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        (0..QUEENS_COUNT).forEach { x ->
            val shift = Math.PI / 2
            val offsetY = sin(time + x + shift) * BOUNCE_OFFSET
            Queen(
                modifier = Modifier.offset(y = offsetY.dp)
            )
        }
    }
}

@Composable
private fun Queen(
    tint: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier.size(32.dp),
        painter = painterResource(R.drawable.ic_queen),
        tint = tint,
        contentDescription = null,
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
                .padding(horizontal = 16.dp)
                .testTag(WinTestTags.MAIN_MENU_BUTTON),
            onClick = onMainMenuClick
        ) {
            Text(stringResource(R.string.main_menu))
        }
    }
}

private const val QUEENS_COUNT = 8
private const val BOUNCE_OFFSET = 16f

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
