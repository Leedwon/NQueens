package com.ledwon.jakub.nqueens.tests.win

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.ledwon.jakub.nqueens.features.win.WinTestTags

class WinRobot(private val composeTestRule: ComposeTestRule) {

    fun assertTopBarTitleIsDisplayed(): WinRobot = apply {
        composeTestRule.onNodeWithText("Congratulations")
            .assertIsDisplayed()
    }

    fun assertYouWinWithElapsedTime(elapsedTime: String): WinRobot = apply {
        composeTestRule.onNodeWithTag(WinTestTags.YOUR_TIME)
            .assertTextEquals("Your time $elapsedTime")
    }

    fun clickMainMenuButton(): WinRobot = apply {
        composeTestRule.onNodeWithTag(WinTestTags.MAIN_MENU_BUTTON)
            .performClick()
    }
}
