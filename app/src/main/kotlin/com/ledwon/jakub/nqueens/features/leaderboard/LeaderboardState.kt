package com.ledwon.jakub.nqueens.features.leaderboard

import kotlinx.collections.immutable.ImmutableList

data class LeaderboardState(
    val boardSizes: ImmutableList<Int>,
    val selectedBoardSize: Int?,
    val entries: ImmutableList<LeaderboardEntry>,
    val isEmpty: Boolean,
    val isLoading: Boolean
)

data class LeaderboardEntry(
    val place: Int,
    val elapsedMillis: Long
)
