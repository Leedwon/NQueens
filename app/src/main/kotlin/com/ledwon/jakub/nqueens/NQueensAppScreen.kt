package com.ledwon.jakub.nqueens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            MainMenuScreen(
                navigateToGame = {
                    TODO()
                }
            )
        }
    }
}
