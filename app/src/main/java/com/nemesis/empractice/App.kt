package com.nemesis.empractice

import android.app.Application
import com.nemesis.core.di.DaggerCoreComponent
import com.nemesis.empractice.di.AppComponent
import com.nemesis.empractice.di.DaggerAppComponent
import com.nemesis.feature_home.di.DaggerHomeComponent

class App : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        val coreComponent = DaggerCoreComponent.factory().create("Application module")
        val homeComponent = DaggerHomeComponent.factory().create(coreComponent)
        appComponent = DaggerAppComponent.factory().create(homeComponent)
    }

}