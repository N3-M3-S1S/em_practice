package com.nemesis.empractice

class Car private constructor(builder: Builder) {
    val manufacturer: String? = builder.manufacturer
    val color: String? = builder.color

    class Builder {
        var manufacturer: String? = null
            private set

        var color: String? = null
            private set

        fun setManufacturer(manufacturer: String): Builder = apply {
            this.manufacturer = manufacturer
        }

        fun setColor(color: String): Builder = apply {
            this.color = color
        }

        fun build(): Car {
            return Car(this)
        }
    }
}
