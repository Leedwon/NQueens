package com.ledwon.jakub.nqueens.core.stopwatch

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeStopwatch : Stopwatch {

    var valueFlow = MutableStateFlow(0L)

    override fun start(): Flow<Long> {
        valueFlow.value =0L
        return valueFlow
    }
}
