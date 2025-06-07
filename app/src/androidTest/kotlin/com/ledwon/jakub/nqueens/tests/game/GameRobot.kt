package com.ledwon.jakub.nqueens.tests.game

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.ledwon.jakub.nqueens.ui.components.ChessBoardTestTags

class GameRobot(private val rule: ComposeTestRule) {

    fun assertBoardSizeIs(size: Int): GameRobot = apply {
        // Check top-left cell
        rule.onNodeWithTag(ChessBoardTestTags.createCellTag(0, 0)).assertExists()

        // Check bottom-right cell
        rule.onNodeWithTag(ChessBoardTestTags.createCellTag(size - 1, size - 1)).assertExists()
    }
}
