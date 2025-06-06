package com.ledwon.jakub.nqueens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.ledwon.jakub.nqueens.features.game.GameDestination
import com.ledwon.jakub.nqueens.features.game.GameScreen
import com.ledwon.jakub.nqueens.features.leaderboard.LeaderboardDestination
import com.ledwon.jakub.nqueens.features.leaderboard.LeaderboardScreen
import com.ledwon.jakub.nqueens.features.mainmenu.MainMenuDestination
import com.ledwon.jakub.nqueens.features.mainmenu.MainMenuScreen
import com.ledwon.jakub.nqueens.features.win.WinDestination
import com.ledwon.jakub.nqueens.features.win.WinScreen

@Composable
fun NQueensAppScreen() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainMenuDestination
    ) {
        composable<MainMenuDestination> {
            MainMenuScreen(
                navigateToGame = { navController.navigate(GameDestination(boardSize = it)) },
                navigateToLeaderboard = { navController.navigate(LeaderboardDestination) }
            )
        }
        composable<GameDestination> {
            val boardSize = it.toRoute<GameDestination>().boardSize
            GameScreen(
                boardSize = boardSize,
                navigateToWinScreen = { elapsedMillis ->
                    navController.navigate(
                        WinDestination(elapsedMillis = elapsedMillis),
                        navOptions = navOptions {
                            popUpTo(MainMenuDestination)
                        })
                },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable<WinDestination> {
            val elapsedMillis = it.toRoute<WinDestination>().elapsedMillis
            WinScreen(
                elapsedMillis = elapsedMillis,
                navigateToMainMenu = {
                    navController.popBackStack(route = MainMenuDestination, inclusive = false)
                }
            )
        }
        composable<LeaderboardDestination> {
            LeaderboardScreen(navigateBack = { navController.popBackStack() })
        }
    }
}
