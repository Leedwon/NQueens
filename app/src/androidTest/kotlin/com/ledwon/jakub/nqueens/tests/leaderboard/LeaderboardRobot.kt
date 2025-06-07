package com.ledwon.jakub.nqueens.tests.leaderboard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText

class LeaderboardRobot(private val rule: ComposeTestRule) {

    fun assertTopBarTitleIsDisplayed(): LeaderboardRobot = apply {
        rule.onNodeWithText("Leaderboards")
            .assertIsDisplayed()
    }
}
