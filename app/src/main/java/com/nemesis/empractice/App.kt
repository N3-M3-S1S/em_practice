package com.nemesis.empractice

import android.app.Application
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    lateinit var postApi: PostApi

    override fun onCreate() {
        super.onCreate()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostApi.BASE_URI)
            .build()

        postApi = retrofit.create(PostApi::class.java)
    }
}
