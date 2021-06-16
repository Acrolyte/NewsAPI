 package com.example.newsapi.api

import android.content.Context
import com.example.newsapi.util.Constants.Companion.BASE_URL
import com.example.newsapi.util.InternetConnectivity
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    private const val cacheSize = (10 * 1024 * 1024).toLong()
    fun getClient(context: Context): SimpleApi {

        val cache: Cache = Cache(context.cacheDir, cacheSize)

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (InternetConnectivity.isNetworkAvailable(context)!!)
                    request.newBuilder().removeHeader("Pragma").header("Cache-Control", "public, max-age=" + 5).build()
                else
                    request.newBuilder()
                        .removeHeader("Pragma")
                        .header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + 60 * 60
                    ).build()
                chain.proceed(request)
            }
            .build()

        val retrofit by lazy {
            Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()
        }
        val api: SimpleApi by lazy {
            retrofit.create(SimpleApi::class.java)
        }
        return api
    }
}