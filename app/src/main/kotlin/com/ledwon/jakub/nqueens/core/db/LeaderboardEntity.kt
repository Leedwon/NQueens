package com.ledwon.jakub.nqueens.core.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leaderboard")
data class LeaderboardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val boardSize: Int,
    val elapsedMillis: Long
)
