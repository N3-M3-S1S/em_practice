package com.nemesis.empractice.domain

data class Bouquet(
    val flowers: Map<Flower, Int>,
    val decorations: Set<BouquetDecoration> = emptySet()
)
