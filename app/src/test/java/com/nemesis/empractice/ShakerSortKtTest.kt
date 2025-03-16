package com.nemesis.empractice

import org.junit.Assert.assertEquals
import org.junit.Test

class ShakerSortTest {

    @Test
    fun shakerSort() {
        val sortedLists = listOf(
            listOf(1, 2, 3, 4, 5, null, null, null),
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2),
            listOf(null),
            listOf(1, null)
        )

        sortedLists.forEach { sortedList ->
            val shuffledList = sortedList.shuffled()
            assertEquals(sortedList, shakerSort(shuffledList))
        }
    }

    @Test
    fun shakerSortNullList() {
        assertEquals(emptyList<Int?>(), shakerSort(null))
    }
}