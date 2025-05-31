package com.ledwon.jakub.nqueens.features.mainmenu

import androidx.lifecycle.SavedStateHandle
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

    private val savedStateHandle = SavedStateHandle()
    private val viewModel = MainMenuViewModel(savedStateHandle)

    @Test
    fun `has correct initial state`() {
        val expected = MainMenuState(boardSize = 8)
        val actual = viewModel.state.value
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `changes board size`() = runTest {
        viewModel.state.test {
            val initial = awaitItem()
            assertThat(initial.boardSize).isEqualTo(8)

            val newSize = 10
            viewModel.onBoardSizeChange(newSize)

            val updated = awaitItem()
            assertThat(updated.boardSize).isEqualTo(newSize)

            cancelAndIgnoreRemainingEvents()
        }
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
