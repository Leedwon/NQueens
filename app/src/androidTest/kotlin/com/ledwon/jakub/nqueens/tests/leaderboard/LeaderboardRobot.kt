package com.ledwon.jakub.nqueens.tests.leaderboard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.ledwon.jakub.nqueens.features.leaderboard.LeaderboardTestTags

class LeaderboardRobot(private val composeTestRule: ComposeTestRule) {

    fun assertTopBarTitleIsDisplayed(): LeaderboardRobot = apply {
        composeTestRule.onNodeWithText("Leaderboard")
            .assertIsDisplayed()
    }

    fun assertEmptyLeaderboardIsDisplayed(): LeaderboardRobot = apply {
        composeTestRule.onNodeWithText("No scores yet")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Solve NQueens at least once to preview your scores")
            .assertIsDisplayed()
    }

    fun assertLeaderboardPlaceIsDisplayed(place: Int): LeaderboardRobot = apply {
        composeTestRule.onNodeWithText("#$place")
            .assertIsDisplayed()
    }

    fun assertLeaderboardTimeIsDisplayed(time: String): LeaderboardRobot = apply {
        composeTestRule.onNodeWithText(time)
            .assertIsDisplayed()
    }

    fun assertBoardSizePickerHasEntries(vararg sizes: Int): LeaderboardRobot = apply {
        sizes.forEach { size ->
            composeTestRule.onNodeWithTag(LeaderboardTestTags.createBoardSizePickerItemTag(size))
                .assertIsDisplayed()
        }
    }

    fun selectBoardSize(size: Int): LeaderboardRobot = apply {
        composeTestRule.onNodeWithTag(LeaderboardTestTags.createBoardSizePickerItemTag(size))
            .performClick()
    }

    fun openBoardSizePicker(): LeaderboardRobot = apply {
        composeTestRule.onNodeWithTag(LeaderboardTestTags.BOARD_SIZE_PICKER)
            .performClick()
    }
}
