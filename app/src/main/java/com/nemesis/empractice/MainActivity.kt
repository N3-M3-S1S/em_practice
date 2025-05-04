package com.nemesis.empractice

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.compose.ui.util.fastFilter
import androidx.compose.ui.util.fastJoinToString
import androidx.lifecycle.lifecycleScope
import com.nemesis.empractice.domain.Bouquet
import com.nemesis.empractice.domain.BouquetDecoration
import com.nemesis.empractice.domain.Flower
import com.nemesis.empractice.domain.FlowerData
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.random.Random

private const val LOG_TAG = "FlowerShop"

class MainActivity : ComponentActivity(R.layout.main) {
    private val flowerShopService by lazy { (application as App).flowerShopService }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeFlowersDataAndLog()
        observePurchasedBouquetsAndLog()
        findViewById<Button>(R.id.button).setOnClickListener {
            buyRandomBouquet()
        }
    }

    private fun observeFlowersDataAndLog() {
        flowerShopService
            .getFlowersData()
            .filter { it.isNotEmpty() }
            .onEach { flowersData ->
                Log.d(
                    LOG_TAG, "Flowers data:\n" +
                            flowersData.fastJoinToString(separator = "\n") { it.toString() })
            }
            .launchIn(lifecycleScope)
    }

    private fun observePurchasedBouquetsAndLog() {
        flowerShopService
            .getPurchasedBouquets()
            .filter { it.isNotEmpty() }
            .onEach { purchasedBouquets ->
                Log.d(
                    LOG_TAG, "Purchased bouquets:\n" +
                            purchasedBouquets.fastJoinToString(separator = "\n") { it.toString() })
            }
            .launchIn(lifecycleScope)
    }

    private fun buyRandomBouquet() {
        lifecycleScope.launch {
            val flowersData = getFlowersDataForAvailableFlowers()
            val bouquet = generateRandomBouquet(flowersData)

            val result = flowerShopService.buyBouquet(bouquet)
            if (result.isFailure) {
                Log.d(LOG_TAG, "Cannot create bouquet: ${result.exceptionOrNull()}")
            }
        }
    }

    private suspend fun getFlowersDataForAvailableFlowers(): List<FlowerData> {
        while (true) {
            val flowersData = flowerShopService
                .getFlowersData()
                .first()
                .fastFilter { it.availableAmount > 0 }

            val allFlowersPurchased = flowersData.isEmpty()
            if (allFlowersPurchased) {
                Log.d(
                    LOG_TAG,
                    "All flowers are purchased, deleting purchased bouquets and restocking flowers"
                )
                flowerShopService.deleteAllPurchasedBouquetsAndRestockFlowers()
            } else {
                return flowersData
            }
        }
    }

    private fun generateRandomBouquet(flowersData: List<FlowerData>): Bouquet {
        val invalidBouquetChance = 0.10f

        val createEmptyBouquet = Random.nextFloat() < invalidBouquetChance
        if (createEmptyBouquet) {
            Log.d(LOG_TAG, "Creating empty bouquet")
            return Bouquet(flowers = emptyMap(), decorations = emptySet())
        }

        var randomFlowers = flowersData
            .shuffled()
            .take(Random.nextInt(1, flowersData.size + 1))
            .associate {
                val addMoreFlowersThanAvailable = Random.nextFloat() < invalidBouquetChance
                val flowersToAdd = if (addMoreFlowersThanAvailable) {
                    Log.d(LOG_TAG, "Adding more flowers than available to bouquet")
                    it.availableAmount + 1
                } else {
                    Random.nextInt(it.availableAmount) + 1
                }
                it.flower to flowersToAdd
            }

        val addNonExistentFlower = Random.nextFloat() < invalidBouquetChance
        if (addNonExistentFlower) {
            Log.d(LOG_TAG, "Adding non existent flower to bouquet")
            randomFlowers = randomFlowers.toMutableMap().also {
                it[Flower(name = "NonExistentFlower", country = "")] = 1
            }
        }

        val decorations = enumValues<BouquetDecoration>()
        decorations.shuffle()

        val randomDecorations = decorations
            .take(Random.nextInt(0, decorations.size + 1))
            .toSet()

        return Bouquet(flowers = randomFlowers, decorations = randomDecorations)
    }
}
