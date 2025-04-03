package com.nemesis.empractice

import android.app.Application
import android.os.Build

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ChargerConnectedNotificationWork.createNotificationChannel(this)
        }
    }

}