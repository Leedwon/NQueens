package com.ledwon.jakub.nqueens.tests.game

import com.ledwon.jakub.nqueens.BaseTestCase
import com.ledwon.jakub.nqueens.core.stopwatch.Stopwatch
import com.ledwon.jakub.nqueens.tests.FakeStopwatch
import com.ledwon.jakub.nqueens.tests.mainmenu.MainMenuRobot
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class GameTests : BaseTestCase() {

    private lateinit var gameRobot: GameRobot

    @Inject
    lateinit var stopwatch: Stopwatch
    
    @Before
    override fun setUp() {
        super.setUp()
        gameRobot = GameRobot(composeTestRule)
        navigateToGameScreen()
    }

    private fun navigateToGameScreen(boardSize: Int = 4) {
        val mainMenuRobot = MainMenuRobot(composeTestRule)
        mainMenuRobot
            .setBoardSize(boardSize.toFloat())
            .clickPlayButton()
    }

    @Test
    fun placesQueenOnBoard() {
        gameRobot
            .assertQueensCount(0)
            .clickOnCell(row = 0, column = 0)
            .assertCellHasQueen(row = 0, column = 0)
            .assertQueensCount(1)
            .clickOnCell(row = 0, column = 0)
            .assertQueensCount(0)
    }

    @Test
    fun detectsConflicts() {
        gameRobot
            .clickOnCell(row = 0, column = 0)
            .clickOnCell(row = 1, column = 0)
            .assertConflictOnCell(row = 0, column = 0)
            .assertConflictOnCell(row = 1, column = 0)
            .assertConflictOnCell(row = 2, column = 0)
            .assertConflictOnCell(row = 3, column = 0)
            .clickOnCell(row = 0, column = 0)
            .clickOnCell(row = 1, column = 0)
            .assertNoConflictOnCell(row = 0, column = 0)
            .assertNoConflictOnCell(row = 1, column = 0)
            .assertNoConflictOnCell(row = 2, column = 0)
            .assertNoConflictOnCell(row = 3, column = 0)
    }

    @Test
    fun updatesElapsedTime() {
        emitTime(1_000L)
        gameRobot.assertElapsedTime("00:01.000")

        emitTime(5_000L)
        gameRobot.assertElapsedTime("00:05.000")

        emitTime(120_000L)
        gameRobot.assertElapsedTime("02:00.000")
    }

    @Test
    fun restartsBoard() {
        gameRobot
            .clickOnCell(row = 0, column = 0)
            .clickOnCell(row = 1, column = 1)
            .clickOnCell(row = 2, column = 0)
            .assertQueensCount(3)
            .assertConflictOnCell(row = 0, column = 0)
            .clickRestartButton()
            .assertQueensCount(0)
            .assertNoConflictOnCell(row = 0, column = 0)
    }

    private fun emitTime(time: Long) {
        (stopwatch as FakeStopwatch).emitTime(time)
    }
}
