package com.ledwon.jakub.nqueens.tests.game

import com.ledwon.jakub.nqueens.BaseTestCase
import com.ledwon.jakub.nqueens.tests.FakeStopwatch
import com.ledwon.jakub.nqueens.tests.mainmenu.MainMenuRobot
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class GameTests : BaseTestCase() {

    private lateinit var gameRobot: GameRobot
    
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
}
