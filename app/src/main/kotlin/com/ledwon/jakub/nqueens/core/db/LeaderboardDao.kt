package com.ledwon.jakub.nqueens.core.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LeaderboardDao {

    @Insert
    suspend fun insert(entry: LeaderboardEntity)

    @Query("SELECT * FROM leaderboard ORDER BY boardSize ASC, elapsedMillis ASC")
    fun getLeaderboards(): Flow<List<LeaderboardEntity>>
}
