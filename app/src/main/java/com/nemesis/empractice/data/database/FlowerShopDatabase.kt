package com.nemesis.empractice.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import com.nemesis.empractice.data.dao.BouquetsDao
import com.nemesis.empractice.data.dao.FlowersDao
import com.nemesis.empractice.data.entity.BouquetEntity
import com.nemesis.empractice.data.entity.BouquetFlowerJunctionEntity
import com.nemesis.empractice.data.entity.FlowerDataDatabaseView
import com.nemesis.empractice.data.entity.FlowerEntity
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

@Database(
    entities = [
        FlowerEntity::class,
        BouquetEntity::class,
        BouquetFlowerJunctionEntity::class
    ],
    views = [FlowerDataDatabaseView::class],
    version = 2
)
abstract class FlowerShopDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: FlowerShopDatabase? = null

        private val MIGRATION_1_2 = Migration(1, 2) { db ->
            db.execSQL(
                """
                ALTER TABLE FlowerEntity
                ADD COLUMN country TEXT NOT NULL DEFAULT "Неизвестно";
                """.trimIndent()
            )

            db.execSQL(
                """
                ALTER TABLE BouquetEntity
                ADD COLUMN decorations TEXT NOT NULL DEFAULT "";
                """.trimIndent()
            )

            db.execSQL("DROP VIEW FlowerDataDatabaseView")

            db.execSQL(
                """
            |CREATE VIEW `FlowerDataDatabaseView` AS SELECT FlowerEntity.name as flowerName, FlowerEntity.country as flowerCountry, FlowerEntity.amount as availableAmount, SUM(BouquetFlowerJunctionEntity.flowerAmount) as reservedAmount FROM FlowerEntity 
            |    LEFT JOIN BouquetFlowerJunctionEntity ON FlowerEntity.id = BouquetFlowerJunctionEntity.flowerId
            |    GROUP BY FlowerEntity.name
            """.trimMargin()
            )
        }

        @Synchronized
        fun getInstance(context: Context): FlowerShopDatabase {
            if (INSTANCE == null) {
                INSTANCE = buildDatabase(context)
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): FlowerShopDatabase {
            return Room
                .databaseBuilder(context, FlowerShopDatabase::class.java, "db")
                .addMigrations(MIGRATION_1_2)
                .addCallback(object : Callback() {
                    override fun onCreate(connection: SQLiteConnection) {
                        INSTANCE?.let { prepopulateFlowers(it.flowersDao()) }
                    }
                })
                .build()
        }

        private fun prepopulateFlowers(flowersDao: FlowersDao) {
            Executors.newSingleThreadExecutor().submit {
                runBlocking {
                    val countries = arrayOf("Россия", "США", "Япония", "Канада", "Бразилия")
                    val flowersMaxAmount = 20
                    val flowerEntities = arrayOf(
                        "Роза",
                        "Тюльпан",
                        "Лилия",
                        "Астра",
                        "Хризантема",
                        "Гвоздика",
                        "Фиалка",
                        "Ромашка",
                        "Мак",
                        "Камиля"
                    ).map {
                        FlowerEntity(
                            name = it,
                            amount = flowersMaxAmount,
                            country = countries.random()
                        )
                    }
                    flowersDao.insertAll(flowerEntities)
                }
            }
        }
    }


    abstract fun flowersDao(): FlowersDao

    abstract fun bouquetsDao(): BouquetsDao
}
