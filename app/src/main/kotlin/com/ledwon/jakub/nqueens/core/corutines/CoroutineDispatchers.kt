package com.ledwon.jakub.nqueens.core.corutines

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface CoroutineDispatchers {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val mainImmediate: CoroutineDispatcher
    val default: CoroutineDispatcher
}

internal class CoroutineDispatchersImpl @Inject constructor() : CoroutineDispatchers {

    override val io: CoroutineDispatcher = Dispatchers.IO

    override val main: CoroutineDispatcher = Dispatchers.Main

    override val mainImmediate: CoroutineDispatcher = Dispatchers.Main.immediate

    override val default: CoroutineDispatcher = Dispatchers.Default
}

@Module
@InstallIn(SingletonComponent::class)
internal interface CoroutineDispatchersModule {

    @Binds
    fun bindCoroutineDispatchers(impl: CoroutineDispatchersImpl): CoroutineDispatchers
}
