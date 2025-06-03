package com.ledwon.jakub.nqueens.features.game

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledwon.jakub.nqueens.core.corutines.CoroutineDispatchers
import com.ledwon.jakub.nqueens.core.game.Conflicts
import com.ledwon.jakub.nqueens.core.game.GameEngineFactory
import com.ledwon.jakub.nqueens.core.stopwatch.Stopwatch
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import com.ledwon.jakub.nqueens.core.game.BoardPosition as CoreBoardPosition
import com.ledwon.jakub.nqueens.core.game.GameState as CoreGameState

@Stable
@HiltViewModel(assistedFactory = GameViewModel.Factory::class)
class GameViewModel @AssistedInject constructor(
    @Assisted private val boardSize: Int,
    private val gameEngineFactory: GameEngineFactory,
    private val stopwatch: Stopwatch,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(boardSize: Int): GameViewModel
    }

    private val _state = MutableStateFlow(createInitialState())
    val state = _state.asStateFlow()

    private var gameEngine = gameEngineFactory.create(boardSize = boardSize)
    private var gameSubscription: Job? = null

    init {
        observeGameFlow()
    }

    fun onCellClick(position: BoardPosition) {
        gameEngine.toggleQueen(position = position.toCorePosition())
    }

    fun onRestartClick() {
        gameSubscription?.cancel()
        gameEngine = gameEngineFactory.create(boardSize = boardSize)
        observeGameFlow()
    }

    private fun observeGameFlow() {
        gameSubscription = viewModelScope.launch {
            combine(
                gameEngine.state,
                stopwatch.start()
            ) { gameState, elapsedMillis ->
                createState(gameState = gameState, elapsedMillis = elapsedMillis)
            }
                .flowOn(dispatchers.default)
                .collect { _state.value = it }
        }
    }

    private fun createState(gameState: CoreGameState, elapsedMillis: Long): GameState {
        return GameState(
            boardSize = boardSize,
            cells = createCells(
                queens = gameState.queens,
                conflicts = gameState.conflicts
            ),
            queensMetadata = createQueensMetadata(
                queens = gameState.queens,
                conflicts = gameState.conflicts
            ),
            elapsedMillis = elapsedMillis
        )
    }

    private fun createCells(
        queens: Set<CoreBoardPosition>,
        conflicts: Conflicts
    ): ImmutableMap<BoardPosition, Cell> {
        return (0 until boardSize).flatMap { row ->
            (0 until boardSize).map { column ->
                val position = CoreBoardPosition(row, column)
                val cell = Cell(
                    hasQueen = position in queens,
                    hasConflict = position in conflicts
                )
                position.toUiPosition() to cell
            }
        }
            .toMap()
            .toPersistentMap()
    }

    private fun createQueensMetadata(
        queens: Set<CoreBoardPosition>,
        conflicts: Conflicts
    ): QueensMetadata {
        val correctlyPlaced = queens.count { it !in conflicts }
        val conflicting = queens.size - correctlyPlaced
        return QueensMetadata(
            goal = boardSize,
            correctlyPlaced = correctlyPlaced,
            conflicting = conflicting
        )
    }

    private fun createInitialState(): GameState {
        return GameState(
            boardSize = boardSize,
            cells = createCells(
                queens = emptySet(),
                conflicts = Conflicts(
                    rows = emptySet(),
                    columns = emptySet(),
                    majorDiagonals = emptySet(),
                    minorDiagonals = emptySet()
                )
            ),
            queensMetadata = QueensMetadata(
                goal = boardSize,
                correctlyPlaced = 0,
                conflicting = 0
            ),
            elapsedMillis = 0
        )
    }

    private fun CoreBoardPosition.toUiPosition(): BoardPosition =
        BoardPosition(row = row, column = column)

    private fun BoardPosition.toCorePosition(): CoreBoardPosition =
        CoreBoardPosition(row = row, column = column)
}
