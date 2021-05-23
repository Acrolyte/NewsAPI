package com.example.newsapi.Models


import com.squareup.moshi.Json

data class Source(
    @Json(name = "id")
    var id: Any,
    @Json(name = "name")
    var name: String = ""
)