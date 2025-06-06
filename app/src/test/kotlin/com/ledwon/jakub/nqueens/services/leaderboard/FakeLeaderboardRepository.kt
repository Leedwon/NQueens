package com.ledwon.jakub.nqueens.services.leaderboard

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLeaderboardRepository : LeaderboardRepository {

    val leaderboards = MutableStateFlow(
        listOf(
            Leaderboard(boardSize = 4, elapsedMillis = listOf(1000L, 1200L, 1400L)),
            Leaderboard(boardSize = 5, elapsedMillis = listOf(2000L, 2200L, 2400L))
        )
    )

    var lastSavedBoardSize: Int? = null
    var lastSavedElapsedMillis: Long? = null

    override suspend fun saveScore(boardSize: Int, elapsedMillis: Long) {
        lastSavedElapsedMillis = elapsedMillis
        lastSavedBoardSize = boardSize
    }

    override fun getLeaderboards(): Flow<List<Leaderboard>> {
        return leaderboards
    }
}
