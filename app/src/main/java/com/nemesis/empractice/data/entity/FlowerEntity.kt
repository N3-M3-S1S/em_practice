package com.nemesis.empractice.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name"], unique = true)])
data class FlowerEntity(
    val name: String,
    val amount: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)
