package com.ledwon.jakub.nqueens.tests.mainmenu

import com.ledwon.jakub.nqueens.BaseTestCase
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
class MainMenuTests : BaseTestCase() {

    @Test
    fun changesBoardSize() {
        val robot = MainMenuRobot(composeTestRule)

        robot
            .assertBoardSize("8 x 8")
            .setBoardSize(4f)
            .assertBoardSize("4 x 4")
            .setBoardSize(8f)
            .assertBoardSize("8 x 8")
    }
}
