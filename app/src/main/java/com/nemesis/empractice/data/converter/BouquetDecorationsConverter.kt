package com.nemesis.empractice.data.converter

import androidx.room.TypeConverter
import com.nemesis.empractice.domain.BouquetDecoration

private const val DELIMITER = ","

class BouquetDecorationsConverter {

    @TypeConverter
    fun fromDecorationsToString(decorations: Set<BouquetDecoration>): String {
        return decorations.joinToString(separator = DELIMITER) { it.name }
    }

    @TypeConverter
    fun fromStringToDecorations(decorationsString: String): Set<BouquetDecoration> {
        val namesWithDecorations = enumValues<BouquetDecoration>().associateBy { it.name }
        return decorationsString.split(DELIMITER).mapNotNullTo(mutableSetOf()) { decorationName ->
            namesWithDecorations[decorationName]
        }
    }
}
