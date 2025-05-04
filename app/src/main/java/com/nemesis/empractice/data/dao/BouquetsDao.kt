package com.nemesis.empractice.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nemesis.empractice.data.entity.BouquetEntity
import com.nemesis.empractice.data.entity.BouquetFlowerJunctionEntity
import com.nemesis.empractice.data.entity.FlowerFromBouquetPojo
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BouquetsDao {

    @Insert
    abstract suspend fun insertBouquetEntity(entity: BouquetEntity): Long

    @Insert
    abstract suspend fun insertBouquetFlowerJunctionEntities(entities: List<BouquetFlowerJunctionEntity>)

    @Query("SELECT * FROM BouquetEntity")
    abstract fun getBouquetEntities(): Flow<List<BouquetEntity>>

    @Query(
        """
        SELECT name, BouquetFlowerJunctionEntity.flowerAmount as amountInBouquet from FlowerEntity
        JOIN BouquetFlowerJunctionEntity on FlowerEntity.id = BouquetFlowerJunctionEntity.flowerId
        WHERE BouquetFlowerJunctionEntity.bouquetId = :bouquetId
        """
    )
    abstract suspend fun getFlowersFromBouquetPojos(bouquetId: Long): List<FlowerFromBouquetPojo>

    @Query("DELETE FROM BouquetEntity")
    abstract suspend fun deleteAll()
}
