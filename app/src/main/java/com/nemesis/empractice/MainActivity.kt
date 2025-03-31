package com.nemesis.empractice

import android.graphics.Color
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.isInvisible
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONObject
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

const val RANDOM_FACT_API_URL = "https://uselessfacts.jsph.pl/api/v2/facts/random"

class MainActivity : ComponentActivity(R.layout.main) {
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = findViewById<TextView>(R.id.textView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val button = findViewById<TextView>(R.id.button)

        val defaultTextViewColor = textView.currentTextColor
        val errorTextViewColor = Color.RED

        button.setOnClickListener {
            disposable = createRandomFactNetworkRequest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    textView.setTextColor(defaultTextViewColor)
                    textView.isInvisible = true
                    progressBar.isInvisible = false
                }
                .doFinally {
                    textView.isInvisible = false
                    progressBar.isInvisible = true
                }
                .doOnError {
                    textView.setTextColor(errorTextViewColor)
                }
                .subscribe { text, error ->
                    if (text != null) {
                        textView.text = text
                    } else if (error != null) {
                        textView.text = error.message ?: "Error"
                    }
                }
        }
    }

    private fun createRandomFactNetworkRequest() = Single.create { emitter ->
        val connection =
            URL(RANDOM_FACT_API_URL).openConnection() as HttpURLConnection
        emitter.setCancellable(connection::disconnect)
        try {
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val responseJson = connection
                    .inputStream.bufferedReader()
                    .use(BufferedReader::readText)
                    .let(::JSONObject)
                emitter.onSuccess(responseJson.getString("text"))
            } else {
                error("HTTP request failed, response code: $responseCode")
            }
        } catch (e: Throwable) {
            emitter.onError(e)
        } finally {
            connection.disconnect()
        }
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

}


