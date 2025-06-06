package com.ledwon.jakub.nqueens.services.leaderboard

import com.google.common.truth.Truth.assertThat
import com.ledwon.jakub.nqueens.core.db.FakeLeaderboardDao
import com.ledwon.jakub.nqueens.core.db.LeaderboardEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LeaderboardRepositoryImplTest {

    private val leaderboardDao = FakeLeaderboardDao()
    private val repository = LeaderboardRepositoryImpl(leaderboardDao = leaderboardDao)

    @Test
    fun `saves score correctly`() = runTest {
        val boardSize = 8
        val elapsedMillis = 1234L

        repository.saveScore(boardSize, elapsedMillis)

        assertThat(leaderboardDao.lastInsertedEntity).isEqualTo(
            LeaderboardEntity(
                boardSize = boardSize,
                elapsedMillis = elapsedMillis
            )
        )
    }

    @Test
    fun `returns leaderboards grouped by board size`() = runTest {
        val expectedLeaderboards = listOf(
            Leaderboard(boardSize = 4, elapsedMillis = listOf(1000L, 1200L, 1400L)),
            Leaderboard(boardSize = 5, elapsedMillis = listOf(2000L, 2200L, 2400L))
        )

        val leaderboards = repository.getLeaderboards().first()
        assertThat(leaderboards).isEqualTo(expectedLeaderboards)
    }
}
