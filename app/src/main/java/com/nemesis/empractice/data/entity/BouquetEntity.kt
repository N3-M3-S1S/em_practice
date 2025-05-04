package com.nemesis.empractice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nemesis.empractice.domain.Bouquet

@Entity
data class BouquetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)

fun Bouquet.toEntity(): BouquetEntity = BouquetEntity()
