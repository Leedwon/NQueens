package com.ledwon.jakub.nqueens.features.leaderboard

import com.google.common.truth.Truth.assertThat
import com.ledwon.jakub.nqueens.services.leaderboard.Leaderboard
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test

class LeaderboardStateFactoryTest {

    private val factory = LeaderboardStateFactory()

    @Test
    fun `creates empty state when no leaderboards are provided`() {
        val state = factory.create(
            selectedBoardSize = 4,
            leaderboards = emptyList()
        )

        assertThat(state).isEqualTo(
            LeaderboardState(
                isEmpty = true,
                isLoading = false,
                boardSizes = persistentListOf(),
                selectedBoardSize = null,
                entries = persistentListOf()
            )
        )
    }

    @Test
    fun `creates state with selected board size when it exists in leaderboards`() {
        val leaderboards = listOf(
            Leaderboard(boardSize = 4, elapsedMillis = listOf(1000L)),
            Leaderboard(boardSize = 5, elapsedMillis = listOf(2000L))
        )

        val state = factory.create(
            selectedBoardSize = 4,
            leaderboards = leaderboards
        )

        assertThat(state).isEqualTo(
            LeaderboardState(
                isEmpty = false,
                isLoading = false,
                boardSizes = persistentListOf(4, 5),
                selectedBoardSize = 4,
                entries = persistentListOf(
                    LeaderboardEntry(place = 1, elapsedMillis = 1000L)
                )
            )
        )
    }

    @Test
    fun `creates state with first board size when selected does not exist in leaderboards`() {
        val leaderboards = listOf(
            Leaderboard(boardSize = 5, elapsedMillis = listOf(2000L)),
            Leaderboard(boardSize = 6, elapsedMillis = listOf(3000L))
        )

        val state = factory.create(
            selectedBoardSize = 4,
            leaderboards = leaderboards
        )

        assertThat(state).isEqualTo(
            LeaderboardState(
                isEmpty = false,
                isLoading = false,
                boardSizes = persistentListOf(5, 6),
                selectedBoardSize = 5,
                entries = persistentListOf(
                    LeaderboardEntry(place = 1, elapsedMillis = 2000L)
                )
            )
        )
    }

    @Test
    fun `creates state with multiple entries for selected board size`() {
        val leaderboards = listOf(
            Leaderboard(boardSize = 4, elapsedMillis = listOf(1000L, 1500L, 2000L)),
            Leaderboard(boardSize = 5, elapsedMillis = listOf(2500L))
        )

        val state = factory.create(
            selectedBoardSize = 4,
            leaderboards = leaderboards
        )

        assertThat(state).isEqualTo(
            LeaderboardState(
                isEmpty = false,
                isLoading = false,
                boardSizes = persistentListOf(4, 5),
                selectedBoardSize = 4,
                entries = persistentListOf(
                    LeaderboardEntry(place = 1, elapsedMillis = 1000L),
                    LeaderboardEntry(place = 2, elapsedMillis = 1500L),
                    LeaderboardEntry(place = 3, elapsedMillis = 2000L)
                )
            )
        )
    }
}
