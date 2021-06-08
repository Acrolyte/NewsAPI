package com.example.newsapi.api

import android.content.Context
import com.example.newsapi.util.CheckConnect
import com.example.newsapi.util.Constants.Companion.BASE_URL
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    fun getClient(context: Context): SimpleApi {
        var cache: Cache = Cache(context.cacheDir, 10 * 1024 * 1024)
        var okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(Interceptor { chain ->
                var request: Request = chain.request()
                request = if (CheckConnect.checkConnectivity(context)!!)
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                else
                    request.newBuilder().header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + 60 * 60 * 5
                    ).build()
                chain.proceed(request)
            })
            .build()


        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }
        val api: SimpleApi by lazy {
            retrofit.create(SimpleApi::class.java)
        }
        return api
    }

}