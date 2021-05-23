package com.example.newsapi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.newsapi.databinding.FragmentWebBinding


class WebFragment : Fragment() {

    private lateinit var binding: FragmentWebBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebBinding.inflate(layoutInflater)

        binding.wvWebfrag.webViewClient = WebViewClient()
        var webSettings = binding.wvWebfrag.settings
        webSettings.javaScriptEnabled = true
        var bundle = this.arguments
        var newurl : String = ""
        if (bundle != null) {
            newurl = bundle.getString("url").toString()
        }
        binding.wvWebfrag.loadUrl(newurl)
        Log.d("Webfrag: ", newurl)


        return binding.root
    }

}