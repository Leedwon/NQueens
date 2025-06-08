package com.ledwon.jakub.nqueens.features.game

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ledwon.jakub.nqueens.core.corutines.CoroutineRule
import com.ledwon.jakub.nqueens.core.game.GameEngine
import com.ledwon.jakub.nqueens.core.game.GameEngineFactory
import com.ledwon.jakub.nqueens.core.stopwatch.FakeStopwatch
import com.ledwon.jakub.nqueens.features.game.GameViewModel.UiEffect.NavigateToWinScreen
import com.ledwon.jakub.nqueens.services.leaderboard.FakeLeaderboardRepository
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class GameViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    private val coroutineRule = CoroutineRule(testDispatcher)

    private val boardSize = 4
    private val winningPositions = listOf(
        BoardPosition(0, 1),
        BoardPosition(1, 3),
        BoardPosition(2, 0),
        BoardPosition(3, 2)
    )
    private val gameEngineFactory = object : GameEngineFactory {
        override fun create(boardSize: Int): GameEngine {
            return GameEngine(boardSize = boardSize)
        }
    }
    private val gameStateFactory = GameStateFactory()

    private val leaderboardRepository = FakeLeaderboardRepository()
    private val stopwatch = FakeStopwatch()

    private val viewModel = GameViewModel(
        boardSize = boardSize,
        gameStateFactory = gameStateFactory,
        gameEngineFactory = gameEngineFactory,
        leaderboardRepository = leaderboardRepository,
        stopwatch = stopwatch,
        dispatchers = coroutineRule.testDispatchers
    )

    @Test
    fun `has correct initial state`() {
        val expectedState = GameState(
            boardSize = boardSize,
            cells = emptyCells().toPersistentMap(),
            queensMetadata = QueensMetadata(
                goal = boardSize,
                correctlyPlaced = 0,
                conflicting = 0
            ),
            elapsedMillis = 0
        )

        assertThat(viewModel.state.value).isEqualTo(expectedState)
    }

    @Test
    fun `adds queen on cell click`() = runTest(testDispatcher) {
        viewModel.state.test {
            awaitItem() // Initial state

            val position = BoardPosition(row = 0, column = 0)
            viewModel.onCellClick(position)

            val expected = emptyCells()
                .apply { set(position, Cell(hasQueen = true, hasConflict = false)) }
                .toPersistentMap()

            assertThat(awaitItem().cells).isEqualTo(expected)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `removes queen on cell click when already present`() = runTest(testDispatcher) {
        viewModel.state.test {
            awaitItem() // Initial state

            val position = BoardPosition(row = 0, column = 0)
            viewModel.onCellClick(position)

            val expected = emptyCells()
                .apply { set(position, Cell(hasQueen = true, hasConflict = false)) }
                .toPersistentMap()

            assertThat(awaitItem().cells).isEqualTo(expected)

            viewModel.onCellClick(position)
            assertThat(awaitItem().cells).isEqualTo(emptyCells())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updates elapsed time`() = runTest(testDispatcher) {
        viewModel.state.test {
            awaitItem() // Initial state

            stopwatch.valueFlow.value = 5000L
            assertThat(awaitItem().elapsedMillis).isEqualTo(5000L)

            stopwatch.valueFlow.value = 10000L
            assertThat(awaitItem().elapsedMillis).isEqualTo(10000L)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `restarts game`() = runTest(testDispatcher) {
        viewModel.state.test {
            awaitItem() // Initial state

            val position = BoardPosition(row = 0, column = 0)
            viewModel.onCellClick(position)
            stopwatch.valueFlow.value = 1000L

            val mutatedGameState = GameState(
                boardSize = boardSize,
                cells = emptyCells()
                    .apply { set(position, Cell(hasQueen = true, hasConflict = false)) }
                    .toPersistentMap(),
                queensMetadata = QueensMetadata(
                    goal = boardSize,
                    correctlyPlaced = 1,
                    conflicting = 0
                ),
                elapsedMillis = 1000L
            )

            assertThat(awaitItem()).isEqualTo(mutatedGameState)

            viewModel.onRestartClick()

            val initialGameState = GameState(
                boardSize = boardSize,
                cells = emptyCells().toPersistentMap(),
                queensMetadata = QueensMetadata(
                    goal = boardSize,
                    correctlyPlaced = 0,
                    conflicting = 0
                ),
                elapsedMillis = 0
            )
            assertThat(awaitItem()).isEqualTo(initialGameState)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `navigates to win screen when game is won`() = runTest(testDispatcher) {
        viewModel.uiEffect.test {
            winningPositions.forEach { viewModel.onCellClick(it) }
            stopwatch.valueFlow.value = 123

            assertThat(awaitItem()).isEqualTo(NavigateToWinScreen(123))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `navigates to win screen after restarting game`() = runTest(testDispatcher) {
        viewModel.uiEffect.test {
            stopwatch.valueFlow.value = 123
            winningPositions.take(3).forEach { viewModel.onCellClick(it) }
            expectNoEvents()

            viewModel.onRestartClick()
            stopwatch.valueFlow.value = 456
            winningPositions.forEach { viewModel.onCellClick(it) }
            assertThat(awaitItem()).isEqualTo(NavigateToWinScreen(456))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `saves score when game is won`() = runTest(testDispatcher) {
        winningPositions.forEach { viewModel.onCellClick(it) }
        stopwatch.valueFlow.value = 123
        runCurrent()

        assertThat(leaderboardRepository.lastSavedBoardSize).isEqualTo(boardSize)
        assertThat(leaderboardRepository.lastSavedElapsedMillis).isEqualTo(123)
    }


    private fun emptyCell() = Cell(
        hasQueen = false,
        hasConflict = false
    )

    private fun emptyCells(): MutableMap<BoardPosition, Cell> {
        return (0 until boardSize).flatMap { row ->
            (0 until boardSize).map { column ->
                BoardPosition(row, column) to emptyCell()
            }
        }
            .toMap()
            .toMutableMap()
    }
}
