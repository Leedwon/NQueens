package com.ledwon.jakub.nqueens.features.game

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledwon.jakub.nqueens.core.corutines.CoroutineDispatchers
import com.ledwon.jakub.nqueens.core.game.GameEngineFactory
import com.ledwon.jakub.nqueens.core.stopwatch.Stopwatch
import com.ledwon.jakub.nqueens.features.game.GameViewModel.UiEffect.NavigateToWinScreen
import com.ledwon.jakub.nqueens.services.leaderboard.LeaderboardRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import com.ledwon.jakub.nqueens.core.game.BoardPosition as CoreBoardPosition

@Stable
@HiltViewModel(assistedFactory = GameViewModel.Factory::class)
class GameViewModel @AssistedInject constructor(
    @Assisted private val boardSize: Int,
    private val gameEngineFactory: GameEngineFactory,
    private val gameStateFactory: GameStateFactory,
    private val leaderboardRepository: LeaderboardRepository,
    private val stopwatch: Stopwatch,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(boardSize: Int): GameViewModel
    }

    private val _state = MutableStateFlow(createInitialState())
    val state = _state.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    private var gameEngine = gameEngineFactory.create(boardSize = boardSize)
    private var timeFlow = stopwatch.start()
    private var gameStateJob: Job? = null
    private var gameWonJob: Job? = null

    init {
        observeGame()
    }

    fun onCellClick(position: BoardPosition) {
        gameEngine.toggleQueen(position = position.toCorePosition())
    }

    fun onRestartClick() {
        gameStateJob?.cancel()
        gameWonJob?.cancel()
        gameEngine = gameEngineFactory.create(boardSize = boardSize)
        timeFlow = stopwatch.start()
        observeGame()
    }

    private fun observeGame() {
        observeGameState()
        observeHasWon()
    }

    private fun observeGameState() {
        gameStateJob = viewModelScope.launch {
            combine(
                gameEngine.state,
                timeFlow
            ) { gameState, elapsedMillis ->
                gameStateFactory.createState(
                    boardSize = boardSize,
                    gameState = gameState,
                    elapsedMillis = elapsedMillis
                )
            }
                .flowOn(dispatchers.default)
                .collect { _state.value = it }
        }
    }

    private fun observeHasWon() {
        gameWonJob = viewModelScope.launch {
            combine(
                gameEngine.state,
                timeFlow
            ) { gameState, elapsedMillis ->
                gameState.hasWon to elapsedMillis
            }
                .filter { (hasWon, _) -> hasWon }
                .take(1)
                .collect { (_, elapsedMillis) ->
                    leaderboardRepository.saveScore(
                        boardSize = boardSize,
                        elapsedMillis = elapsedMillis
                    )
                    _uiEffect.emit(NavigateToWinScreen(elapsedMillis = elapsedMillis))
                }
        }
    }

    private fun createInitialState(): GameState {
        return GameState(
            boardSize = boardSize,
            cells = createEmptyCells(),
            queensMetadata = QueensMetadata(
                goal = boardSize,
                correctlyPlaced = 0,
                conflicting = 0
            ),
            elapsedMillis = 0
        )
    }

    private fun createEmptyCells(
    ): ImmutableMap<BoardPosition, Cell> {
        return (0 until boardSize).flatMap { row ->
            (0 until boardSize).map { column ->
                val cell = Cell(
                    hasQueen = false,
                    hasConflict = false
                )
                BoardPosition(row = row, column = column) to cell
            }
        }
            .toMap()
            .toPersistentMap()
    }

    private fun BoardPosition.toCorePosition(): CoreBoardPosition =
        CoreBoardPosition(row = row, column = column)

    sealed interface UiEffect {
        data class NavigateToWinScreen(val elapsedMillis: Long) : UiEffect
    }
}
