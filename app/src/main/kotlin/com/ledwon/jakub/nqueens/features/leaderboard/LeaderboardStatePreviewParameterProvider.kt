package com.ledwon.jakub.nqueens.features.leaderboard

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.persistentListOf

class LeaderboardStatePreviewParameterProvider : PreviewParameterProvider<LeaderboardState> {

    override val values: Sequence<LeaderboardState> = sequenceOf(
        LeaderboardState(
            isEmpty = false,
            isLoading = false,
            boardSizes = persistentListOf(4, 5, 6),
            selectedBoardSize = 4,
            entries = persistentListOf(
                LeaderboardEntry(place = 1, elapsedMillis = 1234L),
                LeaderboardEntry(place = 2, elapsedMillis = 3456L),
                LeaderboardEntry(place = 3, elapsedMillis = 152512L),
                LeaderboardEntry(place = 4, elapsedMillis = 123123L),
                LeaderboardEntry(place = 5, elapsedMillis = 987654L),
                LeaderboardEntry(place = 6, elapsedMillis = 456789L),
                LeaderboardEntry(place = 7, elapsedMillis = 321654L),
                LeaderboardEntry(place = 8, elapsedMillis = 654321L),
                LeaderboardEntry(place = 9, elapsedMillis = 789123L),
                LeaderboardEntry(place = 10, elapsedMillis = 123456L)
            )
        ),
        LeaderboardState(
            isEmpty = true,
            isLoading = false,
            boardSizes = persistentListOf(),
            selectedBoardSize = null,
            entries = persistentListOf()
        ),
        LeaderboardState(
            isEmpty = false,
            isLoading = true,
            boardSizes = persistentListOf(4, 5, 6),
            selectedBoardSize = null,
            entries = persistentListOf()
        )
    )
}
