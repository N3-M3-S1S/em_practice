package com.nemesis.empractice.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nemesis.empractice.data.entity.FlowerDataDatabaseView
import com.nemesis.empractice.data.entity.FlowerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlowersDao {

    @Insert
    suspend fun insertAll(flowerEntities: List<FlowerEntity>)

    @Update
    suspend fun updateAll(flowerEntities: List<FlowerEntity>)

    @Query("UPDATE FlowerEntity SET amount = :amount")
    suspend fun restockAllFlowers(amount: Int)

    @Query("SELECT * FROM FlowerEntity WHERE name in (:names)")
    suspend fun getByNames(names: List<String>): List<FlowerEntity>

    @Query("SELECT * FROM FlowerDataDatabaseView")
    fun getFlowersDataViews(): Flow<List<FlowerDataDatabaseView>>
}
