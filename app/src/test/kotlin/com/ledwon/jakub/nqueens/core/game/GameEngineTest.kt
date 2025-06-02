package com.ledwon.jakub.nqueens.core.game

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GameEngineTest {

    private val size = 8
    private val engine = GameEngine(boardSize = size)

    @Test
    fun `adds queen`() {
        val queenPosition = BoardPosition(row = 0, column = 0)
        engine.toggleQueen(queenPosition)
        assertThat(engine.state.value.queens).contains(queenPosition)
    }

    @Test
    fun `removes queen`() {
        val queenPosition = BoardPosition(row = 0, column = 0)
        engine.toggleQueen(queenPosition)
        assertThat(engine.state.value.queens).contains(queenPosition)
        engine.toggleQueen(queenPosition)
        assertThat(engine.state.value.queens).doesNotContain(queenPosition)
    }

    @Test
    fun `calculates same row conflict`() {
        val firstQueen = BoardPosition(row = 0, column = 0)
        val secondQueen = BoardPosition(row = 0, column = 1)

        engine.toggleQueen(firstQueen)
        engine.toggleQueen(secondQueen)
        assertThat(engine.state.value.conflicts.rows).containsExactly(0)
    }

    @Test
    fun `calculates same column conflict`() {
        val firstQueen = BoardPosition(row = 0, column = 0)
        val secondQueen = BoardPosition(row = 1, column = 0)

        engine.toggleQueen(firstQueen)
        engine.toggleQueen(secondQueen)

        assertThat(engine.state.value.conflicts.columns).containsExactly(0)
    }

    @Test
    fun `calculates same major diagonal conflict`() {
        val firstQueen = BoardPosition(row = 0, column = 0)
        val secondQueen = BoardPosition(row = 1, column = 1)

        engine.toggleQueen(firstQueen)
        engine.toggleQueen(secondQueen)

        assertThat(engine.state.value.conflicts.majorDiagonals).containsExactly(0)
    }

    @Test
    fun `calculates same minor diagonal conflict`() {
        val firstQueen = BoardPosition(row = 0, column = 7)
        val secondQueen = BoardPosition(row = 1, column = 6)

        engine.toggleQueen(firstQueen)
        engine.toggleQueen(secondQueen)

        assertThat(engine.state.value.conflicts.minorDiagonals).containsExactly(7)
    }

    @Test
    fun `has won should be true`() {
        val nonConflictingQueens = listOf(
            BoardPosition(0, 0),
            BoardPosition(1, 4),
            BoardPosition(2, 7),
            BoardPosition(3, 5),
            BoardPosition(4, 2),
            BoardPosition(5, 6),
            BoardPosition(6, 1),
            BoardPosition(7, 3)
        )

        nonConflictingQueens.forEach { engine.toggleQueen(it) }

        assertThat(engine.state.value.hasWon).isTrue()
        assertThat(engine.state.value.conflicts).isEqualTo(
            Conflicts(
                rows = emptySet(),
                columns = emptySet(),
                majorDiagonals = emptySet(),
                minorDiagonals = emptySet()
            )
        )
    }
}
