package com.ledwon.jakub.nqueens.features.game

import com.google.common.truth.Truth.assertThat
import kotlinx.collections.immutable.persistentMapOf
import kotlin.test.Test

class GameViewModelTest {

    private val viewModel = GameViewModel(boardSize = 8)

    @Test
    fun `has correct initial state`() {
        val expectedState = GameState(
            boardSize = 8,
            cells = persistentMapOf<BoardPosition, Cell>()
        )

        assertThat(viewModel.state.value).isEqualTo(expectedState)
    }
}