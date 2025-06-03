package com.ledwon.jakub.nqueens.core.corutines

import kotlinx.coroutines.CoroutineDispatcher

class TestCoroutineDispatchers(
    private val dispatcher: CoroutineDispatcher
) : CoroutineDispatchers {
    override val io: CoroutineDispatcher = dispatcher
    override val main: CoroutineDispatcher = dispatcher
    override val mainImmediate: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
}
