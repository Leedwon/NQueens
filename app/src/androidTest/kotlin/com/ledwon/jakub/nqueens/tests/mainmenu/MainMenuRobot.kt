package com.ledwon.jakub.nqueens.tests.mainmenu

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performSemanticsAction
import com.ledwon.jakub.nqueens.features.mainmenu.MainMenuTestTags

class MainMenuRobot(private val rule: ComposeTestRule) {

    fun assertBoardSize(expected: String): MainMenuRobot = apply {
        rule.onNodeWithTag(MainMenuTestTags.BOARD_SIZE_TEXT)
            .assertTextEquals(expected)
    }

    fun setBoardSize(value: Float): MainMenuRobot = apply {
        rule.onNodeWithTag(MainMenuTestTags.BOARD_SIZE_SLIDER)
            .performSemanticsAction(SemanticsActions.SetProgress) { it(value) }

        rule.waitForIdle()
    }
}
