package com.nemesis.empractice

import android.app.Application

class App : Application(), AppStartLogger by AppStartLoggerImpl() {

    override fun onCreate() {
        super.onCreate()
        startAppStartTimeLogging()
    }

}