package com.ledwon.jakub.nqueens.services.leaderboard

data class Leaderboard(
    val boardSize: Int,
    val elapsedMillis: List<Long>
)
