package com.example.newsapi.api

import android.app.Application
import com.example.newsapi.util.Constants.Companion.BASE_URL
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    private var application = Application()
    var cache: Cache = Cache(application.cacheDir, 10*1024*1024)
    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor(Interceptor { chain ->
            var request: Request = chain.request()
//            if (!checkConnectivity()) {
                val maxStale = 60 * 60 * 24 * 28
                request = request
                    .newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
//            }
            chain.proceed(request)
        })
        .build()


    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
    val api : SimpleApi by lazy {
        retrofit.create(SimpleApi::class.java)
    }

//    fun checkConnectivity() : Boolean{
//        val connectivityManager = application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
//        return if (connectivityManager is ConnectivityManager){
//            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
//            networkInfo?.isConnected ?: false
//        } else false
//    }

}