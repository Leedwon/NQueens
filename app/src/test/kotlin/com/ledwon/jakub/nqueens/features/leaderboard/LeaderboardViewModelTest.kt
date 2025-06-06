package com.ledwon.jakub.nqueens.features.leaderboard

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ledwon.jakub.nqueens.core.corutines.CoroutineRule
import com.ledwon.jakub.nqueens.services.leaderboard.FakeLeaderboardRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class LeaderboardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    private val coroutineRule = CoroutineRule(testDispatcher)

    private val leaderboardRepository = FakeLeaderboardRepository()

    private val viewModel = LeaderboardViewModel(
        leaderboardStateFactory = LeaderboardStateFactory(),
        leaderboardRepository = leaderboardRepository,
        dispatchers = coroutineRule.testDispatchers
    )

    @Test
    fun `has correct initial state`() {
        val expectedState = LeaderboardState(
            isEmpty = false,
            isLoading = true,
            boardSizes = persistentListOf(),
            selectedBoardSize = null,
            entries = persistentListOf()
        )
        assertThat(viewModel.state.value).isEqualTo(expectedState)
    }

    @Test
    fun `loads leaderboard entries`() = runTest(testDispatcher) {
        viewModel.state.test {
            awaitItem() // initial state

            assertThat(awaitItem()).isEqualTo(
                LeaderboardState(
                    isEmpty = false,
                    isLoading = false,
                    boardSizes = persistentListOf(4, 5),
                    selectedBoardSize = 4,
                    entries = persistentListOf(
                        LeaderboardEntry(place = 1, elapsedMillis = 1000),
                        LeaderboardEntry(place = 2, elapsedMillis = 1200),
                        LeaderboardEntry(place = 3, elapsedMillis = 1400)
                    )
                )
            )

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `changes selected board size`() = runTest(testDispatcher) {
        viewModel.state.test {
            awaitItem() // initial state

            viewModel.onBoardSizeChange(5)
            assertThat(awaitItem()).isEqualTo(
                LeaderboardState(
                    isEmpty = false,
                    isLoading = false,
                    boardSizes = persistentListOf(4, 5),
                    selectedBoardSize = 5,
                    entries = persistentListOf(
                        LeaderboardEntry(place = 1, elapsedMillis = 2000),
                        LeaderboardEntry(place = 2, elapsedMillis = 2200),
                        LeaderboardEntry(place = 3, elapsedMillis = 2400)
                    )
                )
            )

            cancelAndConsumeRemainingEvents()
        }
    }
}
