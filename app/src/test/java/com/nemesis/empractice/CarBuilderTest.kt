package com.nemesis.empractice

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class CarBuilderTest {


    @Test
    fun `build car`() {
        val manufacturer = "BMW"
        val color = "Yellow"

        val car = Car.Builder()
            .setManufacturer(manufacturer)
            .setColor(color)
            .build()

        assertEquals(manufacturer, car.manufacturer)
        assertEquals(color, car.color)
    }

    @Test
    fun `build car without parameters`() {
        val car = Car.Builder().build()

        assertNull(car.manufacturer)
        assertNull(car.color)
    }
}
