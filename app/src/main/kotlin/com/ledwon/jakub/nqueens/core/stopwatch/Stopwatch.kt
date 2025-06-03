package com.ledwon.jakub.nqueens.core.stopwatch

import com.ledwon.jakub.nqueens.core.corutines.CoroutineDispatchers
import com.ledwon.jakub.nqueens.core.datetime.DateTimeProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

interface Stopwatch {
    fun start(): Flow<Long>
}

internal class StopwatchImpl @Inject constructor(
    private val dateTimeProvider: DateTimeProvider,
    private val dispatchers: CoroutineDispatchers
) : Stopwatch {

    override fun start(): Flow<Long> {
        val start = now()
        val secondsFlow = flow {
            delay(REFRESH_INTERVAL)
            while (currentCoroutineContext().isActive) {
                emit(getMillis(start = start, current = now()))
                delay(REFRESH_INTERVAL)
            }
        }
        return secondsFlow
            .distinctUntilChanged()
            .buffer(onBufferOverflow = BufferOverflow.DROP_OLDEST)
            .flowOn(context = dispatchers.default)
    }

    private fun now(): Long = dateTimeProvider.now().toEpochMilli()

    private fun getMillis(start: Long, current: Long): Long {
        return (current - start).milliseconds.inWholeMilliseconds
    }

    private companion object {
        val REFRESH_INTERVAL = 16.milliseconds // corresponds to 60Hz refresh rate
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal interface StopwatchModule {

    @Binds
    fun bind(implementation: StopwatchImpl): Stopwatch
}
