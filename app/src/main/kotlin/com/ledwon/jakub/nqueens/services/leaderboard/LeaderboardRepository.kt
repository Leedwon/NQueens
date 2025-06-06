package com.ledwon.jakub.nqueens.services.leaderboard

import com.ledwon.jakub.nqueens.core.db.LeaderboardDao
import com.ledwon.jakub.nqueens.core.db.LeaderboardEntity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface LeaderboardRepository {
    suspend fun saveScore(boardSize: Int, elapsedMillis: Long)
    fun getLeaderboards(): Flow<List<Leaderboard>>
}

class LeaderboardRepositoryImpl @Inject constructor(
    private val leaderboardDao: LeaderboardDao
) : LeaderboardRepository {

    override suspend fun saveScore(boardSize: Int, elapsedMillis: Long) {
        leaderboardDao.insert(LeaderboardEntity(boardSize = boardSize, elapsedMillis = elapsedMillis))
    }

    override fun getLeaderboards(): Flow<List<Leaderboard>> {
        return leaderboardDao.getLeaderboards().map { entities ->
            entities.groupBy { it.boardSize }.map { (boardSize, entries) ->
                Leaderboard(
                    boardSize = boardSize,
                    elapsedMillis = entries.map { it.elapsedMillis }
                )
            }
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal interface LeaderboardRepositoryModule {

    @Binds
    fun bind(repository: LeaderboardRepositoryImpl): LeaderboardRepository
}
