package com.nemesis.empractice.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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
    version = 1
)
abstract class FlowerShopDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: FlowerShopDatabase? = null

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
                    ).map { FlowerEntity(name = it, amount = flowersMaxAmount) }
                    flowersDao.insertAll(flowerEntities)
                }
            }
        }
    }

    abstract fun flowersDao(): FlowersDao

    abstract fun bouquetsDao(): BouquetsDao
}
