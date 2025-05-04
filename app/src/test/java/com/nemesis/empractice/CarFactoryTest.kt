package com.nemesis.empractice

import junit.framework.TestCase.assertEquals
import org.junit.Test

class CarFactoryTest {

    @Test
    fun `test car factories`() {
        var carFactory: CarFactory = RedToyotaFactory()

        var expectedColor = "Red"
        var expectedManufacturer = "Toyota"
        var expectedManufacturerCountry = "Japan"

        var car = carFactory.buildCar()
        var manufacturerCountry = carFactory.getManufacturerCountry()

        assertEquals(expectedColor, car.color)
        assertEquals(expectedManufacturer, car.manufacturer)
        assertEquals(expectedManufacturerCountry, manufacturerCountry)

        carFactory = BlackFordFactory()

        expectedColor = "Black"
        expectedManufacturer = "Ford"
        expectedManufacturerCountry = "USA"

        car = carFactory.buildCar()
        manufacturerCountry = carFactory.getManufacturerCountry()

        assertEquals(expectedColor, car.color)
        assertEquals(expectedManufacturer, car.manufacturer)
        assertEquals(expectedManufacturerCountry, manufacturerCountry)
    }
}
