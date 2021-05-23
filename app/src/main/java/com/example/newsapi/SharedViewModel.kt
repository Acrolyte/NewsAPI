package com.example.newsapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel(){

    private var _url = MutableLiveData("www.google.com")
    var url : LiveData<String> = _url

    fun seturl(newurl : String){
        _url.value = newurl
    }

}