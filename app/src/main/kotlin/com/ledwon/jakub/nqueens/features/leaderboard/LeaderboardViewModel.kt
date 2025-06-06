package com.ledwon.jakub.nqueens.features.leaderboard

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledwon.jakub.nqueens.core.corutines.CoroutineDispatchers
import com.ledwon.jakub.nqueens.services.leaderboard.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Stable
@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    leaderboardRepository: LeaderboardRepository,
    private val leaderboardStateFactory: LeaderboardStateFactory,
    dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val selectedBoardSize = MutableStateFlow(INITIAL_BOARD_SIZE)

    val state = combine(
        selectedBoardSize,
        leaderboardRepository.getLeaderboards(),
    ) { selectedBoardSize, leaderboards ->
        leaderboardStateFactory.create(
            selectedBoardSize = selectedBoardSize,
            leaderboards = leaderboards
        )
    }
        .flowOn(dispatchers.default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LeaderboardState(
                isEmpty = false,
                isLoading = true,
                boardSizes = persistentListOf(),
                selectedBoardSize = null,
                entries = persistentListOf()
            )
        )

    fun onBoardSizeChange(boardSize: Int) {
        selectedBoardSize.value = boardSize
    }

    private companion object {
        const val INITIAL_BOARD_SIZE = 4
    }
}
