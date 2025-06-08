package com.ledwon.jakub.nqueens.features.game

import com.google.common.truth.Truth.assertThat
import com.ledwon.jakub.nqueens.core.game.Conflicts
import kotlinx.collections.immutable.toPersistentMap
import kotlin.test.Test
import com.ledwon.jakub.nqueens.core.game.BoardPosition as CoreBoardPosition
import com.ledwon.jakub.nqueens.core.game.GameState as CoreGameState

class GameStateFactoryTest {

    private val boardSize = 4
    private val factory = GameStateFactory()

    @Test
    fun `creates state with row conflict`() {
        val actual = factory.createState(
            boardSize = boardSize,
            gameState = createCoreGameState(
                queens = setOf(CoreBoardPosition(0, 0), CoreBoardPosition(0, 1)),
                conflicts = createConflicts(rows = setOf(0))
            ),
            elapsedMillis = 0L
        )

        val expectedCells = createEmptyCells(boardSize).apply {
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

        val expected = createGameState(
            cells = expectedCells,
            queensMetadata = createQueensMetadata(goal = 4, correctlyPlaced = 0, conflicting = 2),
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `creates state with column conflict`() {
        val actual = factory.createState(
            boardSize = boardSize,
            gameState = createCoreGameState(
                queens = setOf(CoreBoardPosition(0, 0), CoreBoardPosition(1, 0)),
                conflicts = createConflicts(columns = setOf(0))
            ),
            elapsedMillis = 0L
        )

        val expectedCells = createEmptyCells(boardSize)
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


        val expected = createGameState(
            cells = expectedCells,
            queensMetadata = createQueensMetadata(goal = 4, correctlyPlaced = 0, conflicting = 2)
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `creates state with major diagonal conflict`() {
        val actual = factory.createState(
            boardSize = boardSize,
            gameState = createCoreGameState(
                queens = setOf(CoreBoardPosition(0, 0), CoreBoardPosition(1, 1)),
                conflicts = createConflicts(majorDiagonals = setOf(0))
            ),
            elapsedMillis = 0L
        )

        val expectedCells = createEmptyCells(boardSize).apply {
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

        val expected = createGameState(
            cells = expectedCells,
            queensMetadata = createQueensMetadata(goal = 4, correctlyPlaced = 0, conflicting = 2)
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `creates cells with minor diagonal conflict`() {
        val actual = factory.createState(
            boardSize = boardSize,
            createCoreGameState(
                queens = setOf(CoreBoardPosition(0, 3), CoreBoardPosition(1, 2)),
                conflicts = createConflicts(minorDiagonals = setOf(3))
            ),
            elapsedMillis = 0L
        )

        val expectedCells = createEmptyCells(boardSize).apply {
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

        val expected = createGameState(
            cells = expectedCells,
            queensMetadata = createQueensMetadata(goal = 4, correctlyPlaced = 0, conflicting = 2)
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `creates state with no conflicts when queens are placed correctly`() {
        val positions = listOf(
            CoreBoardPosition(0, 1),
            CoreBoardPosition(1, 3),
            CoreBoardPosition(2, 0),
            CoreBoardPosition(3, 2)
        ).toSet()

        val actual = factory.createState(
            boardSize = boardSize,
            gameState = createCoreGameState(queens = positions, conflicts = createConflicts()),
            elapsedMillis = 0L
        )

        val expectedCells = createEmptyCells(boardSize).apply {
            positions.forEach {
                set(
                    BoardPosition(row = it.row, column = it.column),
                    Cell(hasQueen = true, hasConflict = false)
                )
            }
        }

        val expected = createGameState(
            cells = expectedCells,
            queensMetadata = createQueensMetadata(goal = 4, correctlyPlaced = 4, conflicting = 0),
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `creates state with elapsed time`() {
        val elapsedMillis = 1234L
        val actual = factory.createState(
            boardSize = boardSize,
            gameState = createCoreGameState(),
            elapsedMillis = elapsedMillis
        )

        val expected = createGameState(elapsedMillis = elapsedMillis)

        assertThat(actual).isEqualTo(expected)
    }
}

private fun createGameState(
    boardSize: Int = 4,
    cells: Map<BoardPosition, Cell> = createEmptyCells(boardSize),
    queensMetadata: QueensMetadata = createQueensMetadata(),
    elapsedMillis: Long = 0L
) = GameState(
    boardSize = boardSize,
    cells = cells.toPersistentMap(),
    queensMetadata = queensMetadata,
    elapsedMillis = elapsedMillis
)

private fun createCoreGameState(
    queens: Set<CoreBoardPosition> = emptySet(),
    conflicts: Conflicts = createConflicts(),
    hasWon: Boolean = false
): CoreGameState {
    return CoreGameState(
        queens = queens,
        conflicts = conflicts,
        hasWon = hasWon
    )
}

private fun createConflicts(
    rows: Set<Int> = emptySet(),
    columns: Set<Int> = emptySet(),
    majorDiagonals: Set<Int> = emptySet(),
    minorDiagonals: Set<Int> = emptySet()
) = Conflicts(
    rows = rows,
    columns = columns,
    majorDiagonals = majorDiagonals,
    minorDiagonals = minorDiagonals
)

private fun createEmptyCells(boardSize: Int): MutableMap<BoardPosition, Cell> {
    return (0 until boardSize).flatMap { row ->
        (0 until boardSize).map { column ->
            BoardPosition(row, column) to Cell(
                hasQueen = false,
                hasConflict = false
            )
        }
    }
        .toMap()
        .toMutableMap()
}

private fun createQueensMetadata(
    goal: Int = 4,
    correctlyPlaced: Int = 0,
    conflicting: Int = 0
): QueensMetadata {
    return QueensMetadata(
        goal = goal,
        correctlyPlaced = correctlyPlaced,
        conflicting = conflicting
    )
}
