package com.ledwon.jakub.nqueens.features.leaderboard

import com.ledwon.jakub.nqueens.services.leaderboard.Leaderboard
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

class LeaderboardStateFactory @Inject constructor() {

    fun create(
        selectedBoardSize: Int,
        leaderboards: List<Leaderboard>
    ): LeaderboardState {
        val boardSizes = leaderboards.map { it.boardSize }
        val selectedSize = if (boardSizes.contains(selectedBoardSize)) {
            selectedBoardSize
        } else {
            boardSizes.firstOrNull()
        }
        val elapsedMillis =
            leaderboards.firstOrNull { it.boardSize == selectedSize }?.elapsedMillis.orEmpty()

        return LeaderboardState(
            isEmpty = leaderboards.isEmpty(),
            isLoading = false,
            boardSizes = boardSizes.toPersistentList(),
            selectedBoardSize = selectedSize,
            entries = elapsedMillis.mapIndexed { index, elapsedMillis ->
                LeaderboardEntry(
                    place = index + 1,
                    elapsedMillis = elapsedMillis
                )
            }.toPersistentList()
        )
    }
}
