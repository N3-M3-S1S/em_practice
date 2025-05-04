package com.nemesis.empractice.data.entity

import androidx.room.DatabaseView
import com.nemesis.empractice.domain.Flower
import com.nemesis.empractice.domain.FlowerData

@DatabaseView(
    value = """
    SELECT FlowerEntity.name as flowerName, FlowerEntity.country as flowerCountry, FlowerEntity.amount as availableAmount, SUM(BouquetFlowerJunctionEntity.flowerAmount) as reservedAmount FROM FlowerEntity 
    LEFT JOIN BouquetFlowerJunctionEntity ON FlowerEntity.id = BouquetFlowerJunctionEntity.flowerId
    GROUP BY FlowerEntity.name
    """
)
data class FlowerDataDatabaseView(
    val flowerName: String,
    val flowerCountry: String,
    val availableAmount: Int,
    val reservedAmount: Int
)

fun FlowerDataDatabaseView.toFlowerData(): FlowerData = FlowerData(
    flower = Flower(name = flowerName, country = flowerCountry),
    availableAmount = availableAmount,
    reservedAmount = reservedAmount,
)
