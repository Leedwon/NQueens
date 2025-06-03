package com.ledwon.jakub.nqueens.core.datetime

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Instant

interface DateTimeProvider {
    fun now(): Instant
}

@Module
@InstallIn(SingletonComponent::class)
class DateTimeProviderModule {

    @Provides
    fun providesDateTimeProvider(): DateTimeProvider {
        return object : DateTimeProvider {
            override fun now(): Instant {
                return Instant.now()
            }
        }
    }
}
