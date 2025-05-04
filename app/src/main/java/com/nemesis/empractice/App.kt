package com.nemesis.empractice

import android.app.Application
import com.nemesis.empractice.data.database.FlowerShopDatabase
import com.nemesis.empractice.data.service.FlowerShopServiceImpl
import com.nemesis.empractice.domain.FlowerShopService

class App : Application() {
    lateinit var flowerShopService: FlowerShopService

    override fun onCreate() {
        super.onCreate()
        flowerShopService = FlowerShopServiceImpl(FlowerShopDatabase.getInstance(this))
    }
}
