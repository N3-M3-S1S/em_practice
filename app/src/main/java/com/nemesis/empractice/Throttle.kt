package com.nemesis.empractice

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.time.ComparableTimeMark
import kotlin.time.Duration
import kotlin.time.TimeSource

fun <T> Flow<T>.throttleFirst(
    window: Duration,
    timeSource: TimeSource.WithComparableMarks = TimeSource.Monotonic
): Flow<T> = flow {
    var lastEmitTime: ComparableTimeMark? = null
    collect { value ->
        val now = timeSource.markNow()
        if (lastEmitTime == null || now - lastEmitTime!! > window) {
            lastEmitTime = now
            emit(value)
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.throttleLatest(window: Duration): Flow<T> = channelFlow {
    val latestEmittedValue = produce(capacity = Channel.CONFLATED) {
        collect { value ->
            send(value)
        }
    }

    launch {
        latestEmittedValue.consumeEach { value ->
            send(value)
            delay(window)
        }
    }
}

