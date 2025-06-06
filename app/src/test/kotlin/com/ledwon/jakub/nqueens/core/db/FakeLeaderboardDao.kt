package com.ledwon.jakub.nqueens.core.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLeaderboardDao : LeaderboardDao {

    val topScores = MutableStateFlow(
        listOf(
            LeaderboardEntity(id = 0, boardSize = 4, elapsedMillis = 1000L),
            LeaderboardEntity(id = 1, boardSize = 4, elapsedMillis = 1200L),
            LeaderboardEntity(id = 2, boardSize = 4, elapsedMillis = 1400L),
            LeaderboardEntity(id = 3, boardSize = 5, elapsedMillis = 2000L),
            LeaderboardEntity(id = 4, boardSize = 5, elapsedMillis = 2200L),
            LeaderboardEntity(id = 5, boardSize = 5, elapsedMillis = 2400L),
        )
    )
    var lastInsertedEntity: LeaderboardEntity? = null

    override fun getLeaderboards(): Flow<List<LeaderboardEntity>> {
        return topScores
    }

    override suspend fun insert(entry: LeaderboardEntity) {
        lastInsertedEntity = entry
    }
}
