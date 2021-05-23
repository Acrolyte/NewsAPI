package com.example.newsapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class MainFragment : Fragment() , MyAdapter.OnItemClickListener{

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController
    private var exlist = emptyList<Article>()
    private val myAdapter by lazy {
        MyAdapter(exlist,this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        setupRecyclerView()

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getPost("in", "d4c4e2a3e66e4f4faebe8b09d000ccfb")

        viewModel.myresponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                exlist = response.body()?.articles!!
                response.body()?.let {
                    myAdapter?.setData(exlist)
                }
            } else {
                Toast.makeText(this.context, response.code(), Toast.LENGTH_SHORT).show()
            }
        })
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvMain.adapter = myAdapter
        binding.rvMain.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onItemClick(position: Int) {
    }

}
