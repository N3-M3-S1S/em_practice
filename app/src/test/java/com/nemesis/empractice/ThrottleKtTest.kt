package com.nemesis.empractice

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.testTimeSource
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class ThrottleKtTest {

    @Test
    fun testThrottleFirst(): Unit = runTest {
        val result = mutableListOf<Int>()

        flow {
            var i = 0
            while (true) {
                emit(i++)
                delay(1.seconds)
            }
        }
            .throttleFirst(1.seconds, testTimeSource)
            .take(5)
            .toList(result)
        
        val expectedDuration = 8000L
        assertEquals(expectedDuration, currentTime)

        val expectedResult = listOf(0, 2, 4, 6, 8)
        assertEquals(expectedResult, result)
    }


    @Test
    fun testThrottleLatest() = runTest {
        val result = mutableListOf<Int>()

        flow {
            delay(1.seconds)
            emit(1)

            for (i in 2..5) {
                emit(i)
                delay(250.milliseconds)
            }

            emit(10)
        }.throttleLatest(1.seconds)
            .take(3)
            .toList(result)

        val expectedDuration = 3000L
        assertEquals(expectedDuration, currentTime)

        val expectedResult = listOf(1, 5, 10)
        assertEquals(expectedResult, result)
    }
}