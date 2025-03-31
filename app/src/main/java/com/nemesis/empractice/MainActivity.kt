package com.nemesis.empractice

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

private const val CURRENT_TIMER_VALUE_KEY = "timer"
private const val TIMER_UPDATE_INTERVAL_SECONDS = 1L

class MainActivity : ComponentActivity(R.layout.main) {
    private lateinit var disposable: Disposable
    private var currentTimerValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            currentTimerValue = it.getInt(CURRENT_TIMER_VALUE_KEY)
        }

        val timerText = findViewById<TextView>(R.id.timerText)
        timerText.text = currentTimerValue.toString()

        disposable = createTimerObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                currentTimerValue++
                timerText.text = currentTimerValue.toString()
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_TIMER_VALUE_KEY, currentTimerValue)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private fun createTimerObservable() = Observable
        .interval(TIMER_UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS) // or timer + repeat
}
