package com.nemesis.empractice

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.Worker
import androidx.work.WorkerParameters

class ChargerConnectedNotificationWork(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "charger_connected_notification"
        private const val NOTIFICATION_CHANNEL_NAME = "Charger connected"
        private const val NOTIFICATION_TITLE = "Charger connected"
        private const val NOTIFICATION_ID = 1

        @RequiresApi(Build.VERSION_CODES.O)
        fun createNotificationChannel(appContext: Context) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(appContext)
                .createNotificationChannel(notificationChannel)
        }

        fun getConstraints() = Constraints.Builder()
            .setRequiresCharging(true)
    }

    override fun doWork(): Result { // pass notification mode as parameter instead of checking?
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || notificationPermissionGranted()) {
            showNotification()
        } else {
            showToast()
        }
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun notificationPermissionGranted() = applicationContext
        .checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun showNotification() {
        val notification = NotificationCompat
            .Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(NOTIFICATION_TITLE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
    }

    private fun showToast() {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(applicationContext, NOTIFICATION_TITLE, Toast.LENGTH_SHORT).show()
        }
    }

}