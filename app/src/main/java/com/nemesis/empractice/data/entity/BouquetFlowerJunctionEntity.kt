package com.nemesis.empractice.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["bouquetId", "flowerId"],
    foreignKeys = [
        ForeignKey(
            entity = BouquetEntity::class,
            parentColumns = ["id"],
            childColumns = ["bouquetId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FlowerEntity::class,
            parentColumns = ["id"],
            childColumns = ["flowerId"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class BouquetFlowerJunctionEntity(
    @ColumnInfo(index = true)
    val bouquetId: Long,
    @ColumnInfo(index = true)
    val flowerId: Long,
    val flowerAmount: Int
)

fun Map<FlowerEntity, Int>.toBouquetJunctionEntity(bouquetEntityId: Long): List<BouquetFlowerJunctionEntity> =
    map { (entity, amount) ->
        BouquetFlowerJunctionEntity(
            bouquetEntityId,
            entity.id,
            amount
        )
    }
