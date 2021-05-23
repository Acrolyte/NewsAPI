package com.example.newsapi.Models

import com.squareup.moshi.Json

data class Article(
    @Json(name = "author")
    var author: String = "",
    @Json(name = "content")
    var content: String = "",
    @Json(name = "description")
    var description: String = "",
    @Json(name = "publishedAt")
    var publishedAt: String = "",
    @Json(name = "source")
    var source: Source ,
    @Json(name = "title")
    var title: String = "",
    @Json(name = "url")
    var url: String = "",
    @Json(name = "urlToImage")
    var urlToImage: String = ""
)