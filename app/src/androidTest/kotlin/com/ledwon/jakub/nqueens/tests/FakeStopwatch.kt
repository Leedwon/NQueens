package com.ledwon.jakub.nqueens.tests

import com.ledwon.jakub.nqueens.core.stopwatch.Stopwatch
import com.ledwon.jakub.nqueens.core.stopwatch.StopwatchModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeStopwatch : Stopwatch {

    private val _timeFlow = MutableStateFlow(1L)

    override fun start(): Flow<Long> {
        return _timeFlow
    }

    fun emitTime(time: Long) {
        _timeFlow.value = time
    }
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [StopwatchModule::class]
)
class FakeStopwatchModule {

    @Provides
    fun provideStopwatch(): Stopwatch {
        return FakeStopwatch()
    }
}
