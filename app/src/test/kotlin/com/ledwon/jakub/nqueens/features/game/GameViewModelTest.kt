package com.ledwon.jakub.nqueens.features.game

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ledwon.jakub.nqueens.core.game.GameEngine
import com.ledwon.jakub.nqueens.core.game.GameEngineFactory
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class GameViewModelTest {

    private val boardSize = 4
    private val gameEngineFactory = object : GameEngineFactory {
        override fun create(boardSize: Int): GameEngine {
            return GameEngine(boardSize = boardSize)
        }
    }
    private val viewModel = GameViewModel(
        boardSize = boardSize,
        gameEngineFactory = gameEngineFactory
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
            )
        )

        assertThat(viewModel.state.value).isEqualTo(expectedState)
    }

    @Test
    fun `adds queen on cell click`() = runTest {
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
    fun `removes queen on cell click when already present`() = runTest {
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
    fun `creates cells with row conflict`() = runTest {
        viewModel.state.test {
            awaitItem() // Initial state

            viewModel.onCellClick(BoardPosition(row = 0, column = 0))
            viewModel.onCellClick(BoardPosition(row = 0, column = 1))

            val expectedCells = emptyCells()
                .apply {
                    set(
                        BoardPosition(row = 0, column = 0),
                        Cell(hasQueen = true, hasConflict = true)
                    )
                    set(
                        BoardPosition(row = 0, column = 1),
                        Cell(hasQueen = true, hasConflict = true)
                    )
                    set(
                        BoardPosition(row = 0, column = 2),
                        Cell(hasQueen = false, hasConflict = true)
                    )
                    set(
                        BoardPosition(row = 0, column = 3),
                        Cell(hasQueen = false, hasConflict = true)
                    )
                }
                .toPersistentMap()

            assertThat(awaitItem().cells).isEqualTo(expectedCells)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `creates cells with column conflict`() = runTest {
        viewModel.state.test {
            awaitItem() // Initial state

            viewModel.onCellClick(BoardPosition(row = 0, column = 0))
            viewModel.onCellClick(BoardPosition(row = 1, column = 0))

            val expectedCells = emptyCells()
                .apply {
                    set(
                        BoardPosition(row = 0, column = 0),
                        Cell(hasQueen = true, hasConflict = true)
                    )
                    set(
                        BoardPosition(row = 1, column = 0),
                        Cell(hasQueen = true, hasConflict = true)
                    )
                    set(
                        BoardPosition(row = 2, column = 0),
                        Cell(hasQueen = false, hasConflict = true)
                    )
                    set(
                        BoardPosition(row = 3, column = 0),
                        Cell(hasQueen = false, hasConflict = true)
                    )
                }
                .toPersistentMap()

            assertThat(awaitItem().cells).isEqualTo(expectedCells)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `creates cells with major diagonal conflict`() = runTest {
        viewModel.state.test {
            awaitItem() // Initial state

            viewModel.onCellClick(BoardPosition(row = 0, column = 0))
            viewModel.onCellClick(BoardPosition(row = 1, column = 1))

            val expectedCells = emptyCells()
                .apply {
                    set(
                        BoardPosition(row = 0, column = 0),
                        Cell(hasQueen = true, hasConflict = true)
                    )
                    set(
                        BoardPosition(row = 1, column = 1),
                        Cell(hasQueen = true, hasConflict = true)
                    )
                    set(
                        BoardPosition(row = 2, column = 2),
                        Cell(hasQueen = false, hasConflict = true)
                    )
                    set(
                        BoardPosition(row = 3, column = 3),
                        Cell(hasQueen = false, hasConflict = true)
                    )
                }
                .toPersistentMap()

            assertThat(awaitItem().cells).isEqualTo(expectedCells)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `creates cells with minor diagonal conflict`() = runTest {
        viewModel.state.test {
            awaitItem() // Initial state

            viewModel.onCellClick(BoardPosition(row = 0, column = 3))
            viewModel.onCellClick(BoardPosition(row = 1, column = 2))

            val expectedCells = emptyCells()
                .apply {
                    set(
                        BoardPosition(row = 0, column = 3),
                        Cell(hasQueen = true, hasConflict = true)
                    )
                    set(
                        BoardPosition(row = 1, column = 2),
                        Cell(hasQueen = true, hasConflict = true)
                    )
                    set(
                        BoardPosition(row = 2, column = 1),
                        Cell(hasQueen = false, hasConflict = true)
                    )
                    set(
                        BoardPosition(row = 3, column = 0),
                        Cell(hasQueen = false, hasConflict = true)
                    )
                }
                .toPersistentMap()

            assertThat(awaitItem().cells).isEqualTo(expectedCells)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `creates cells with no conflicts when queens are placed correctly`() = runTest {
        viewModel.state.test {
            awaitItem() // Initial state

            val positions = listOf(
                BoardPosition(0, 1),
                BoardPosition(1, 3),
                BoardPosition(2, 0),
                BoardPosition(3, 2)
            )

            positions.forEach { viewModel.onCellClick(it) }

            val expectedCells = emptyCells()
                .apply {
                    positions.forEach { set(it, Cell(hasQueen = true, hasConflict = false)) }
                }
                .toPersistentMap()

            assertThat(awaitItem().cells).isEqualTo(expectedCells)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `creates queens metadata`() = runTest {
        viewModel.state.test {
            awaitItem() // Initial state

            val positions = listOf(
                BoardPosition(0, 1),
                BoardPosition(1, 2),
                BoardPosition(2, 0),
                BoardPosition(3, 2)
            )

            positions.forEach { viewModel.onCellClick(it) }

            val expected = QueensMetadata(
                goal = boardSize,
                correctlyPlaced = 1,
                conflicting = 3
            )

            assertThat(awaitItem().queensMetadata).isEqualTo(expected)
        }
    }

    @Test
    fun `restarts game`() = runTest {
        viewModel.state.test {
            awaitItem() // Initial state

            val position = BoardPosition(row = 0, column = 0)
            viewModel.onCellClick(position)

            val mutatedGameState = GameState(
                boardSize = boardSize,
                cells = emptyCells()
                    .apply { set(position, Cell(hasQueen = true, hasConflict = false)) }
                    .toPersistentMap(),
                queensMetadata = QueensMetadata(
                    goal = boardSize,
                    correctlyPlaced = 1,
                    conflicting = 0
                )
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
                )
            )
            assertThat(awaitItem()).isEqualTo(initialGameState)

            cancelAndIgnoreRemainingEvents()
        }
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