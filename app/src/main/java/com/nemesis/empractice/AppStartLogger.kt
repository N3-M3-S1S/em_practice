package com.nemesis.empractice

import android.util.Log
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

interface AppStartLogger {
    fun startAppStartTimeLogging()
}

class AppStartLoggerImpl : AppStartLogger {
    private val appStartTime = System.currentTimeMillis()
    private val appStartTimeFormat = SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault())
    private val logDelay = 3.seconds
    private val logTag = "AppStartLogger"

    @OptIn(DelicateCoroutinesApi::class)
    override fun startAppStartTimeLogging() {
        GlobalScope.launch {
            while (isActive) {
                Log.d(
                    logTag,
                    appStartTimeFormat.format(appStartTime)
                )
                delay(logDelay)
            }
        }
    }

}