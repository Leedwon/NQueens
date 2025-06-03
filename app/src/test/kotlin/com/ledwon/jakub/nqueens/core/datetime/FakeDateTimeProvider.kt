package com.ledwon.jakub.nqueens.core.datetime

import java.time.Instant

class FakeDateTimeProvider(initialNow: Instant = Instant.EPOCH) : DateTimeProvider {

    var now = initialNow

    override fun now(): Instant = now
}
