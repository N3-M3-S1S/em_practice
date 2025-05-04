package com.nemesis.empractice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.nemesis.empractice.data.converter.BouquetDecorationsConverter
import com.nemesis.empractice.domain.Bouquet
import com.nemesis.empractice.domain.BouquetDecoration

@Entity
@TypeConverters(BouquetDecorationsConverter::class)
data class BouquetEntity(
    val decorations: Set<BouquetDecoration>,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)

fun Bouquet.toEntity(): BouquetEntity = BouquetEntity(decorations = decorations)
