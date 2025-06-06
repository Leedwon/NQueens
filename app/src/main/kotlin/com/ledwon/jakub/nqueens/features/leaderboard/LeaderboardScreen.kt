package com.ledwon.jakub.nqueens.features.leaderboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledwon.jakub.nqueens.R
import com.ledwon.jakub.nqueens.core.datetime.compose.formatMillis
import com.ledwon.jakub.nqueens.ui.theme.NQueensTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun LeaderboardScreen(
    leaderboardViewModel: LeaderboardViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {

    val state = leaderboardViewModel.state.collectAsStateWithLifecycle()

    LeaderboardScreenContent(
        leaderboardState = state.value,
        onBoardSizeChange = { leaderboardViewModel.onBoardSizeChange(it) },
        onBackClick = navigateBack
    )
}

@Composable
private fun LeaderboardScreenContent(
    leaderboardState: LeaderboardState,
    onBoardSizeChange: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(topBar = {
        TopBar(
            scrollBehavior = scrollBehavior,
            onBackClick = onBackClick
        )
    }) {
        when {
            leaderboardState.isLoading -> Loading(modifier = Modifier.padding(it))
            leaderboardState.isEmpty -> EmptyLeaderboards(modifier = Modifier.padding(it))
            else -> LeaderboardContent(
                modifier = Modifier.padding(it),
                leaderboardState = leaderboardState,
                scrollBehavior = scrollBehavior,
                onBoardSizeChange = onBoardSizeChange
            )
        }
    }
}

@Composable
private fun Loading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyLeaderboards(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.empty_leaderboard_title),
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = stringResource(R.string.empty_leaderboard_message),
        )
    }
}

@Composable
private fun LeaderboardContent(
    leaderboardState: LeaderboardState,
    scrollBehavior: TopAppBarScrollBehavior,
    onBoardSizeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        BoardSizePicker(
            selectedBoardSize = leaderboardState.selectedBoardSize,
            boardSizes = leaderboardState.boardSizes,
            onBoardSizeChange = onBoardSizeChange
        )
        Leaderboard(
            entries = leaderboardState.entries,
            scrollBehavior = scrollBehavior
        )
    }
}

@Composable
private fun BoardSizePicker(
    selectedBoardSize: Int?,
    boardSizes: ImmutableList<Int>,
    onBoardSizeChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(type = PrimaryNotEditable)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = selectedBoardSize?.let {
                stringResource(R.string.board_size_template, selectedBoardSize)
            }.orEmpty(),
            label = { Text(stringResource(R.string.board_size)) },
            readOnly = true,
            onValueChange = {}
        )
        ExposedDropdownMenu(
            modifier = Modifier.padding(horizontal = 16.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            boardSizes.forEach { boardSize ->
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.board_size_template, boardSize)) },
                    onClick = {
                        onBoardSizeChange(boardSize)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun Leaderboard(
    entries: ImmutableList<LeaderboardEntry>,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        items(entries) { entry ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.sizeIn(minWidth = 32.dp),
                    text = stringResource(R.string.leaderboard_place_template, entry.place)
                )
                Text(formatMillis(entry.elapsedMillis))
                Text(getMedalEmoji(place = entry.place))
            }
        }
    }
}

@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(text = stringResource(R.string.leaderboards)) },
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

private fun getMedalEmoji(place: Int): String {
    return when (place) {
        1 -> "\uD83E\uDD47" // 🥇
        2 -> "\uD83E\uDD48" // 🥈
        3 -> "\uD83E\uDD49" // 🥉
        else -> ""
    }
}

@Preview
@Composable
private fun GameScreenPreview(
    @PreviewParameter(LeaderboardStatePreviewParameterProvider::class) state: LeaderboardState
) {
    NQueensTheme {
        LeaderboardScreenContent(
            onBoardSizeChange = {},
            onBackClick = {},
            leaderboardState = state
        )
    }
}
