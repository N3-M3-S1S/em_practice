package com.nemesis.empractice

import retrofit2.http.GET

interface PostApi {
    companion object{
        const val BASE_URI = "https://jsonplaceholder.typicode.com"
    }

    @GET("posts/1")
    suspend fun getPost(): Post
}
