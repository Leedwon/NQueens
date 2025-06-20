package com.ledwon.jakub.nqueens.tests.game

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.ledwon.jakub.nqueens.features.game.GameTestTags
import com.ledwon.jakub.nqueens.ui.components.ChessBoardTestTags

class GameRobot(private val rule: ComposeTestRule) {

    fun assertBoardSizeIs(size: Int): GameRobot = apply {
        // Check top-left cell
        rule.onNodeWithTag(ChessBoardTestTags.createCellTag(0, 0)).assertExists()

        // Check bottom-right cell
        rule.onNodeWithTag(ChessBoardTestTags.createCellTag(size - 1, size - 1)).assertExists()
    }

    fun assertCellHasQueen(row: Int, column: Int): GameRobot = apply {
        rule.onNodeWithTag(ChessBoardTestTags.createCellTag(row, column), useUnmergedTree = true)
            .assert(hasAnyDescendant(hasTestTag(GameTestTags.QUEEN)))
    }

    fun assertQueensCount(count: Int): GameRobot = apply {
        rule.onAllNodesWithTag(GameTestTags.QUEEN, useUnmergedTree = true)
            .assertCountEquals(count)
    }

    fun assertConflictOnCell(row: Int, column: Int): GameRobot = apply {
        assertConflictOnCell(row = row, column = column, expected = true)
    }

    fun assertNoConflictOnCell(row: Int, column: Int): GameRobot = apply {
        assertConflictOnCell(row = row, column = column, expected = false)
    }

    fun assertElapsedTime(expected: String): GameRobot = apply {
        rule.onNodeWithTag(GameTestTags.ELAPSED_TIME)
            .assertTextEquals(expected)
    }

    fun clickOnCell(row: Int, column: Int): GameRobot = apply {
        rule.onNodeWithTag(ChessBoardTestTags.createCellTag(row, column))
            .performClick()
    }

    fun clickRestartButton(): GameRobot = apply {
        rule.onNodeWithTag(GameTestTags.RESTART_BUTTON)
            .performClick()
    }

    fun win4x4Game(): GameRobot = apply {
        clickOnCell(0, 1)
        clickOnCell(1, 3)
        clickOnCell(2, 0)
        clickOnCell(3, 2)
    }

    fun win5x5Game(): GameRobot = apply {
        clickOnCell(0, 3)
        clickOnCell(1, 1)
        clickOnCell(2, 4)
        clickOnCell(3, 2)
        clickOnCell(4, 0)
    }

    fun win8x8Game(): GameRobot = apply {
        clickOnCell(0, 0)
        clickOnCell(1, 4)
        clickOnCell(2, 7)
        clickOnCell(3, 5)
        clickOnCell(4, 2)
        clickOnCell(5, 6)
        clickOnCell(6, 1)
        clickOnCell(7, 3)
    }

    private fun assertConflictOnCell(row: Int, column: Int, expected: Boolean): GameRobot = apply {
        rule.onNodeWithTag(ChessBoardTestTags.createCellTag(row, column), useUnmergedTree = true)
            .assert(
                hasAnyDescendant(
                    SemanticsMatcher.expectValue(
                        GameTestTags.HasConflictKey,
                        expected
                    )
                )
            )
    }
}
