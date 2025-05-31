package com.ledwon.jakub.nqueens.features.mainmenu

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ledwon.jakub.nqueens.core.corutines.CoroutineRule
import com.ledwon.jakub.nqueens.features.mainmenu.MainMenuViewModel.UiEffect.NavigateToGame
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MainMenuViewModelTest {

    @get:Rule
    private val coroutineRule = CoroutineRule()

    private val viewModel = MainMenuViewModel()

    @Test
    fun `has correct initial state`() {
        val expected = MainMenuState(boardSize = 8)
        val actual = viewModel.state.value
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `changes board size`() {
        val newSize = 10
        viewModel.onBoardSizeChange(newSize)
        val actual = viewModel.state.value.boardSize
        assertThat(actual).isEqualTo(newSize)
    }

    @Test
    fun `opens game on play click`() = runTest {
        viewModel.uiEffect.test {
            viewModel.onPlayClick()
            assertThat(awaitItem()).isEqualTo(NavigateToGame(boardSize = 8))

            cancelAndIgnoreRemainingEvents()
        }
    }
}
