package com.nemesis.empractice.domain

import kotlinx.coroutines.flow.Flow

interface FlowerShopService {

    fun getFlowersData(): Flow<List<FlowerData>>

    fun getPurchasedBouquets(): Flow<List<Bouquet>>

    suspend fun buyBouquet(bouquet: Bouquet): Result<Unit>

    suspend fun deleteAllPurchasedBouquetsAndRestockFlowers()
}
