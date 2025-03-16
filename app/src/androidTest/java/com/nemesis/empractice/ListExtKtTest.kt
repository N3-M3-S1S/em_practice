package com.nemesis.empractice

import org.junit.Assert.assertEquals
import org.junit.Test

class ListExtKtTest {
    private val list1 = listOf("s", Any(), 2F, 'c', 1234, IntArray(3) { it })
    private val list2 = emptyList<Any>()
    private val list3 = listOf(1)
    private val list4 = listOf(6,7,8)
    private val list5 = listOf("s", Any())

    @Test
    fun firstInt() {
        assertEquals(1234, list1.firstInt())
        assertEquals(null, list2.firstInt())
        assertEquals(1, list3.firstInt())
        assertEquals(6, list4.firstInt())
        assertEquals(null, list5.firstInt())
    }

    @Test
    fun firstIntAlternative() {
        assertEquals(1234, list1.firstIntAlternative())
        assertEquals(null, list2.firstIntAlternative())
        assertEquals(1, list3.firstIntAlternative())
        assertEquals(6, list4.firstIntAlternative())
        assertEquals(null, list5.firstIntAlternative())
    }
}