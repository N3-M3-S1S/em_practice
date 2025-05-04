package com.nemesis.empractice

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

private const val LOG_TAG = "LoggingInterceptor"

class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        Log.d(
            LOG_TAG,
            "Server responded with code: ${response.code()} to request: ${response.request().url()}"
        )

        return response
    }
}
