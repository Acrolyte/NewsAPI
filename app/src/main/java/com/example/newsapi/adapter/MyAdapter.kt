package com.example.newsapi.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.Models.Article
import com.example.newsapi.R
import com.example.newsapi.databinding.NewsItemBinding
import com.example.newsapi.util.InternetConnectivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso


class MyAdapter(private var myList: List<Article>, private var context: Context) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: NewsItemBinding, itemView: View) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener , View.OnLongClickListener{
        init {
            itemView.setOnClickListener {
            }
        }

        override fun onClick(v: View?) {
        }

        override fun onLongClick(v: View?): Boolean {
         return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(
            NewsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvItemTitle.text = myList[position].title
        Picasso.get().load(myList[position].urlToImage).into(holder.binding.ivItemImage)
        holder.itemView.setOnClickListener {
            if(!InternetConnectivity.isNetworkAvailable(context)!!){
                Snackbar.make(it,"Please check your Internet connection!!",Snackbar.LENGTH_SHORT).show()
            }else {
                var bundle = Bundle()
                bundle.putString("url", myList[position].url.toString())
                it.findNavController().navigate(R.id.action_mainFragment_to_webFragment, bundle)
                Log.d("working", myList[position].url)
            }
        }
        holder.itemView.setOnLongClickListener{
            var timestamp = myList[position].publishedAt.replace('T',' ').replace('Z',' ')
            Snackbar.make(it,"This news was published at ${timestamp}",Snackbar.LENGTH_SHORT).show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    fun setData(newList: List<Article>) {
        myList = newList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}