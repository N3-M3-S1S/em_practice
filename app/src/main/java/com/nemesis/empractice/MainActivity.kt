package com.nemesis.empractice

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.core.widget.doOnTextChanged
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

private const val LOG_TAG = "Debounce"
private const val DEBOUNCE_SECONDS = 3L

class MainActivity : ComponentActivity(R.layout.main) {
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textLogSubject = PublishSubject.create<String>()

        findViewById<EditText>(R.id.editText).doOnTextChanged { text, _, _, _ ->
            textLogSubject.onNext(text.toString())
        }

        disposable = textLogSubject
            .debounce(DEBOUNCE_SECONDS, TimeUnit.SECONDS)
            .subscribe { Log.d(LOG_TAG, it) }
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

}
