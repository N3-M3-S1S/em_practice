package com.nemesis.empractice

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

private const val NOTIFICATION_PERMISSION_DENIED_MESSAGE =
    "Notification permission denied, the toast will be used for notification"

class MainActivity : ComponentActivity(R.layout.main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestNotificationPermission { permissionGranted: Boolean ->
                    if (!permissionGranted) {
                        showNotificationPermissionDeniedToast()
                    }
                    enqueueChargerConnectedNotificationWork()
                }
            } else {
                enqueueChargerConnectedNotificationWork()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission(
        onResult: (permissionGranted: Boolean) -> Unit
    ) {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            onResult(permissionGranted)
        }.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun showNotificationPermissionDeniedToast() {
        Toast.makeText(
            this,
            NOTIFICATION_PERMISSION_DENIED_MESSAGE,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun enqueueChargerConnectedNotificationWork() {
        val workConstraints = ChargerConnectedNotificationWork
            .getConstraints()
            .build()

        val work =
            OneTimeWorkRequestBuilder<ChargerConnectedNotificationWork>()
                .setConstraints(workConstraints)
                .build()

        WorkManager
            .getInstance(this)
            .enqueue(work)
    }


}
