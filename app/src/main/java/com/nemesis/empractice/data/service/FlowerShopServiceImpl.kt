package com.nemesis.empractice.data.service

import androidx.room.withTransaction
import com.nemesis.empractice.data.database.FlowerShopDatabase
import com.nemesis.empractice.data.entity.FlowerDataDatabaseView
import com.nemesis.empractice.data.entity.FlowerEntity
import com.nemesis.empractice.data.entity.toBouquetJunctionEntity
import com.nemesis.empractice.data.entity.toEntity
import com.nemesis.empractice.data.entity.toFlowerData
import com.nemesis.empractice.domain.Bouquet
import com.nemesis.empractice.domain.Flower
import com.nemesis.empractice.domain.FlowerData
import com.nemesis.empractice.domain.FlowerShopService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FlowerShopServiceImpl(private val database: FlowerShopDatabase) : FlowerShopService {
    private val flowersDao = database.flowersDao()
    private val bouquetsDao = database.bouquetsDao()

    override fun getFlowersData(): Flow<List<FlowerData>> {
        return flowersDao.getFlowersDataViews()
            .map { flowerDataViews -> flowerDataViews.map(FlowerDataDatabaseView::toFlowerData) }
    }

    override fun getPurchasedBouquets(): Flow<List<Bouquet>> {
        return bouquetsDao.getBouquetEntities().map { bouquetEntities ->
            bouquetEntities.map { bouquetEntity ->
                val flowersFromBouquet =
                    bouquetsDao.getFlowersFromBouquetPojos(bouquetEntity.id)
                        .associate { flowerFromBouquetPojo ->
                            Flower(flowerFromBouquetPojo.name) to flowerFromBouquetPojo.amountInBouquet
                        }
                Bouquet(flowers = flowersFromBouquet)
            }
        }
    }

    override suspend fun deleteAllPurchasedBouquetsAndRestockFlowers() {
        bouquetsDao.deleteAll()
        val restockFlowersAmount = 20
        flowersDao.restockAllFlowers(restockFlowersAmount)
    }

    override suspend fun buyBouquet(bouquet: Bouquet): Result<Unit> {
        return getFlowerEntitiesAndReservedAmount(bouquet)
            .onSuccess { flowerEntitiesAndReservedAmount ->
                saveBouquetToDatabaseAndUpdateAvailableFlowersAmount(
                    bouquet,
                    flowerEntitiesAndReservedAmount
                )
            }.map { }
    }

    private suspend fun getFlowerEntitiesAndReservedAmount(bouquet: Bouquet): Result<Map<FlowerEntity, Int>> {
        val validFlowersFromBouquet = bouquet.flowers.filter { (_, amount) -> amount >= 1 }

        if (validFlowersFromBouquet.isEmpty()) {
            return Result.failure(IllegalArgumentException("Bouquet is empty"))
        }

        val flowerNamesInBouquet = validFlowersFromBouquet.keys.map { it.name }
        val flowerEntities = flowersDao.getByNames(flowerNamesInBouquet)

        val flowerEntitiesWithReservedAmount = mutableMapOf<FlowerEntity, Int>()

        for ((flower, reservedAmount) in validFlowersFromBouquet) {
            val entityForFlower = flowerEntities.firstOrNull { it.name == flower.name }
            if (entityForFlower != null) {
                if (entityForFlower.amount >= reservedAmount) {
                    flowerEntitiesWithReservedAmount[entityForFlower] = reservedAmount
                } else {
                    return Result.failure(
                        IllegalArgumentException(
                            """
                        Not enough flowers of type '${flower.name}' in the database:
                        requested: $reservedAmount, available: ${entityForFlower.amount}
                            """.trimIndent()
                        )
                    )
                }
            } else {
                return Result.failure(
                    IllegalArgumentException(
                        "Flower '${flower.name}' is missing in the database"
                    )
                )
            }
        }

        return Result.success(flowerEntitiesWithReservedAmount)
    }

    private suspend fun saveBouquetToDatabaseAndUpdateAvailableFlowersAmount(
        bouquet: Bouquet,
        flowerEntityAndReservedAmount: Map<FlowerEntity, Int>
    ) {
        database.withTransaction {
            val bouquetEntityId = bouquetsDao.insertBouquetEntity(bouquet.toEntity())
            val junctionEntities =
                flowerEntityAndReservedAmount.toBouquetJunctionEntity(bouquetEntityId)
            bouquetsDao.insertBouquetFlowerJunctionEntities(junctionEntities)
            updateAvailableFlowersAmount(flowerEntityAndReservedAmount)
        }
    }

    private suspend fun updateAvailableFlowersAmount(
        flowersEntitiesAndReservedAmount: Map<FlowerEntity, Int>
    ) {
        val updatedFlowersEntities =
            flowersEntitiesAndReservedAmount.map { (flowerEntity, reservedAmount) ->
                flowerEntity.copy(amount = flowerEntity.amount - reservedAmount)
            }
        flowersDao.updateAll(updatedFlowersEntities)
    }
}
