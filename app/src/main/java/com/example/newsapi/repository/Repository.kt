package com.example.newsapi.repository

import com.example.newsapi.Models.Post
import com.example.newsapi.api.RetrofitInstance
import retrofit2.Response

class Repository {

    suspend fun getPost(country: String, apiKey: String): Response<Post> {
        return RetrofitInstance.api.getPost(country, apiKey)
    }

}