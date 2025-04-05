package com.nemesis.empractice

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.nemesis.feature_home.usecase.PerformRequestsUseCase
import javax.inject.Inject

class MainActivity : ComponentActivity(R.layout.main) {

    @Inject
    lateinit var performRequestsUseCase: PerformRequestsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
        Log.d("MainActivity", "Request result : ${performRequestsUseCase()}")
    }

}
