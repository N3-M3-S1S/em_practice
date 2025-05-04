package com.nemesis.empractice

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

private const val LOG_TAG = "MainActivity"

class MainActivity : ComponentActivity(R.layout.main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val postApi = (application as App).postApi

        findViewById<Button>(R.id.button).setOnClickListener {
            lifecycleScope.launch {
                try {
                    val post = postApi.getPost()
                    Log.d(LOG_TAG, post.toString())
                } catch (e: Throwable) {
                    Log.e(LOG_TAG, e.message.orEmpty())
                }
            }
        }
    }
}
