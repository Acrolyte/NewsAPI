package com.example.newsapi.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object CheckConnect {

    fun checkConnectivity(context: Context) : Boolean{
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager){
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }
}