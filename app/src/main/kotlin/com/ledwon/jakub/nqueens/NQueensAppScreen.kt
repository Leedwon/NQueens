package com.ledwon.jakub.nqueens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ledwon.jakub.nqueens.features.game.GameDestination
import com.ledwon.jakub.nqueens.features.game.GameScreen
import com.ledwon.jakub.nqueens.features.mainmenu.MainMenuDestination
import com.ledwon.jakub.nqueens.features.mainmenu.MainMenuScreen

@Composable
fun NQueensAppScreen() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainMenuDestination
    ) {
        composable<MainMenuDestination> {
            MainMenuScreen(navigateToGame = { navController.navigate(GameDestination(boardSize = it)) })
        }
        composable<GameDestination> {
            val boardSize = it.toRoute<GameDestination>().boardSize
            GameScreen(
                boardSize = boardSize,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
