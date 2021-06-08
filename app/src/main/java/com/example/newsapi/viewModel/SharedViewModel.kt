package com.example.newsapi.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel(){

    private var _url = MutableLiveData("www.google.com")
    var url : LiveData<String> = _url

    private var phonekey = MutableLiveData("00")
    var pho : LiveData<String> = phonekey

    fun seturl(newurl : String){
        _url.value = newurl
    }

    fun setphone(newphon : String){
        phonekey.value = newphon
    }
}