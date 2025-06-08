package com.ledwon.jakub.nqueens.tests.leaderboard

import com.ledwon.jakub.nqueens.BaseTestCase
import com.ledwon.jakub.nqueens.core.stopwatch.Stopwatch
import com.ledwon.jakub.nqueens.tests.FakeStopwatch
import com.ledwon.jakub.nqueens.tests.game.GameRobot
import com.ledwon.jakub.nqueens.tests.mainmenu.MainMenuRobot
import com.ledwon.jakub.nqueens.tests.win.WinRobot
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class LeaderboardTests : BaseTestCase() {

    private lateinit var leaderboardRobot: LeaderboardRobot
    private lateinit var mainMenuRobot: MainMenuRobot
    private lateinit var gameRobot: GameRobot
    private lateinit var winRobot: WinRobot

    @Inject
    lateinit var stopwatch: Stopwatch

    @Before
    override fun setUp() {
        super.setUp()
        leaderboardRobot = LeaderboardRobot(composeTestRule)
        mainMenuRobot = MainMenuRobot(composeTestRule)
        gameRobot = GameRobot(composeTestRule)
        winRobot = WinRobot(composeTestRule)
    }

    @Test
    fun showsEmptyState() {
        mainMenuRobot.clickLeaderboardButton()
        leaderboardRobot.assertEmptyLeaderboardIsDisplayed()
    }

    @Test
    fun showsLeaderboardEntries() {
        mainMenuRobot.setBoardSize(4f)
        emitTime(10_000L)

        mainMenuRobot.clickPlayButton()
        gameRobot.win4x4Game()
        winRobot.clickMainMenuButton()
        mainMenuRobot.clickLeaderboardButton()

        leaderboardRobot
            .assertLeaderboardPlaceIsDisplayed(place = 1)
            .assertLeaderboardTimeIsDisplayed(time = "00:10.000")
    }

    private fun emitTime(time: Long) {
        (stopwatch as FakeStopwatch).emitTime(time)
    }
}
