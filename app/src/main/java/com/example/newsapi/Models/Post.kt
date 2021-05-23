package com.example.newsapi.Models


import com.squareup.moshi.Json

data class Post(
    @Json(name = "articles")
    var articles: List<Article>,
    @Json(name = "status")
    var status: String = "",
    @Json(name = "totalResults")
    var totalResults: Int = 0
)