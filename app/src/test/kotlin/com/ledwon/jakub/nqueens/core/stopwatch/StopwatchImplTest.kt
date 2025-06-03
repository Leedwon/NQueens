package com.ledwon.jakub.nqueens.core.stopwatch

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ledwon.jakub.nqueens.core.corutines.CoroutineRule
import com.ledwon.jakub.nqueens.core.datetime.FakeDateTimeProvider
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.time.Instant
import kotlin.test.Test

class StopwatchImplTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    private val coroutineRule = CoroutineRule(testDispatcher)

    private val now = Instant.parse("2025-06-03T10:00:00Z")

    private val dateTimeProvider = FakeDateTimeProvider(initialNow = now)

    private val stopwatch = StopwatchImpl(
        dateTimeProvider = dateTimeProvider,
        dispatchers = coroutineRule.testDispatchers
    )

    @Test
    fun `emits elapsed seconds correctly`() = runTest(testDispatcher) {
        stopwatch.start().test {
            assertThat(awaitItem()).isEqualTo(0L)

            dateTimeProvider.now = now.plusSeconds(10)
            assertThat(awaitItem()).isEqualTo(10_000L)

            dateTimeProvider.now = now.plusSeconds(20)
            assertThat(awaitItem()).isEqualTo(20_000L)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `does not emit the same seconds value`() = runTest(testDispatcher) {
        stopwatch.start().test {
            assertThat(awaitItem()).isEqualTo(0L)

            dateTimeProvider.now = now.plusSeconds(10)
            assertThat(awaitItem()).isEqualTo(10_000L)

            dateTimeProvider.now = now.plusSeconds(10)
            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }
}
