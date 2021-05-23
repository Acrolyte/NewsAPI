package com.example.newsapi.api

import com.example.newsapi.Models.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SimpleApi {

    @GET("/v2/top-headlines")
    suspend fun getPost(
        @Query("country")country: String,
        @Query("apiKey") apiKey : String
    ): Response<Post>

}