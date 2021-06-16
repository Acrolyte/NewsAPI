package com.example.newsapi.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapi.Models.Article
import com.example.newsapi.adapter.MyAdapter
import com.example.newsapi.databinding.FragmentMainBinding
import com.example.newsapi.repository.Repository
import com.example.newsapi.viewModel.MainViewModel
import com.example.newsapi.viewModel.MainViewModelFactory

class MainFragment : Fragment() , MyAdapter.OnItemClickListener{

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController
    private var exlist = emptyList<Article>()
    private val myAdapter by lazy {
        MyAdapter(exlist,this.requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        setupRecyclerView()
        val repository = Repository(this.requireContext())
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getPost("in", "d4c4e2a3e66e4f4faebe8b09d000ccfb")

        viewModel.myresponse.observe(viewLifecycleOwner, Observer { response ->
            Log.d("res",response.toString())
            if (response.isSuccessful) {
                Log.d("successdata",response.body().toString())
                exlist = response.body()?.articles!!
                binding.pbBar.visibility = ProgressBar.GONE
                response.body()?.let {
                    myAdapter?.setData(exlist)
                }
            } else {
                Toast.makeText(this.context, "Client offline", Toast.LENGTH_SHORT).show()
                Log.d("cached-data",response.body().toString())
                binding.pbBar.visibility = ProgressBar.GONE
//                response.body()?.let {
//                    myAdapter.setData(exlist)
//                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvMain.adapter = myAdapter
        binding.rvMain.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onItemClick(position: Int) {
    }

}
