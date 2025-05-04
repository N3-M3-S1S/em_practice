package com.nemesis.empractice

interface CarFactory {
    fun buildCar(): Car

    fun getManufacturerCountry(): String
}

class RedToyotaFactory : CarFactory {

    override fun buildCar(): Car {
        return Car.Builder()
            .setManufacturer("Toyota")
            .setColor("Red")
            .build()
    }

    override fun getManufacturerCountry(): String {
        return "Japan"
    }
}

class BlackFordFactory : CarFactory {

    override fun buildCar(): Car {
        return Car.Builder()
            .setManufacturer("Ford")
            .setColor("Black")
            .build()
    }

    override fun getManufacturerCountry(): String {
        return "USA"
    }
}
