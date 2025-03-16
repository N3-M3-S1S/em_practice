package com.nemesis.empractice

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nemesis.empractice.ui.theme.EMPracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EMPracticeTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Button(
                        onClick = ::logListFindResult,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(text = "Log")
                    }
                }
            }
        }
    }

    private fun logListFindResult() {
        val list = listOf("s", Any(), 2F, 'c', 1234, IntArray(3) { it })
        val result = list.firstInt()
        Log.d("MainActivity", "First int in list: $result")
    }

}
