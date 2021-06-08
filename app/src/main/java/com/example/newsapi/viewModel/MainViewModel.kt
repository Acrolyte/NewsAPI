package com.example.newsapi.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapi.Models.Post
import com.example.newsapi.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {

    val myresponse: MutableLiveData<Response<Post>> = MutableLiveData()

    fun getPost(country: String, apiKey: String) {
        viewModelScope.launch {
            val resp = repository.getPost(country, apiKey)
            myresponse.value = resp
        }
    }
}
