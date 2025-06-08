package com.ledwon.jakub.nqueens.tests.e2e

import com.ledwon.jakub.nqueens.BaseTestCase
import com.ledwon.jakub.nqueens.core.stopwatch.Stopwatch
import com.ledwon.jakub.nqueens.tests.FakeStopwatch
import com.ledwon.jakub.nqueens.tests.game.GameRobot
import com.ledwon.jakub.nqueens.tests.leaderboard.LeaderboardRobot
import com.ledwon.jakub.nqueens.tests.mainmenu.MainMenuRobot
import com.ledwon.jakub.nqueens.tests.win.WinRobot
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class EndToEndTests : BaseTestCase() {

    lateinit var mainMenuRobot: MainMenuRobot
    lateinit var gameRobot: GameRobot
    lateinit var winRobot: WinRobot
    lateinit var leaderboardRobot: LeaderboardRobot

    @Inject
    lateinit var stopwatch: Stopwatch

    @Before
    override fun setUp() {
        super.setUp()
        mainMenuRobot = MainMenuRobot(composeTestRule)
        gameRobot = GameRobot(composeTestRule)
        winRobot = WinRobot(composeTestRule)
        leaderboardRobot = LeaderboardRobot(composeTestRule)
    }

    @Test
    fun wins8x8Game() {
        mainMenuRobot.clickPlayButton()

        emitTime(10_000L)
        gameRobot
            .assertBoardSizeIs(8)
            .win8x8Game()
        winRobot
            .assertTopBarTitleIsDisplayed()
            .assertYouWinWithElapsedTime("00:10.000")
    }

    @Test
    fun wins4x4Game() {
        mainMenuRobot
            .setBoardSize(4f)
            .clickPlayButton()

        emitTime(5_000L)
        gameRobot
            .assertBoardSizeIs(4)
            .win4x4Game()
        winRobot
            .assertTopBarTitleIsDisplayed()
            .assertYouWinWithElapsedTime("00:05.000")
    }

    @Test
    fun winsMultipleGamesAndShowsLeaderboard() {
        win4x4Game(elapsedTime = 1_000L)
        win4x4Game(elapsedTime = 2_000L)
        win4x4Game(elapsedTime = 3_000L)
        win5x5Game(elapsedTime = 5_000L)
        win5x5Game(elapsedTime = 6_000L)
        win8x8Game(elapsedTime = 10_000L)

        mainMenuRobot.clickLeaderboardButton()

        leaderboardRobot
            .openBoardSizePicker()
            .assertBoardSizePickerHasEntries(4, 5, 8)
            .selectBoardSize(4)
            .assertLeaderboardPlacesAreDisplayed(1, 2, 3)
            .assertLeaderboardTimesAreDisplayed("00:01.000", "00:02.000", "00:03.000")
            .openBoardSizePicker()
            .selectBoardSize(5)
            .assertLeaderboardPlacesAreDisplayed(1, 2)
            .assertLeaderboardTimesAreDisplayed("00:05.000", "00:06.000")
            .openBoardSizePicker()
            .selectBoardSize(8)
            .assertLeaderboardPlacesAreDisplayed(1)
            .assertLeaderboardTimesAreDisplayed("00:10.000")
    }

    private fun win4x4Game(elapsedTime: Long) {
        mainMenuRobot
            .setBoardSize(4f)
            .clickPlayButton()

        emitTime(elapsedTime)
        gameRobot.win4x4Game()
        winRobot.clickMainMenuButton()
    }

    private fun win5x5Game(elapsedTime: Long) {
        mainMenuRobot
            .setBoardSize(5f)
            .clickPlayButton()

        emitTime(elapsedTime)
        gameRobot.win5x5Game()
        winRobot.clickMainMenuButton()
    }

    private fun win8x8Game(elapsedTime: Long) {
        mainMenuRobot
            .setBoardSize(8f)
            .clickPlayButton()

        emitTime(elapsedTime)
        gameRobot.win8x8Game()
        winRobot.clickMainMenuButton()
    }

    private fun emitTime(time: Long) {
        (stopwatch as FakeStopwatch).emitTime(time)
    }
}
