package com.nemesis.empractice

import android.app.Application

class App(private val appStartLogger: AppStartLogger = AppStartLoggerImpl()) : Application(),
    AppStartLogger by appStartLogger {

    override fun onCreate() {
        super.onCreate()
        startAppStartTimeLogging()
    }

}