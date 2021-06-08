package com.example.newsapi.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.newsapi.R
import com.example.newsapi.databinding.ActivityMainBinding
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.firebase.auth.FirebaseAuth

class  MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.dropmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_option1 -> {
                startActivity(Intent(this, OssLicensesMenuActivity::class.java))
            }
            R.id.btn_option2 ->{
                navController.navigate(R.id.action_mainFragment_to_aboutFragment)
            }
            R.id.btn_option3->{
                navController.navigate(R.id.action_mainFragment_to_detailsFragment)
            }
            R.id.btn_option4->{
                auth.signOut()
                navController.navigate(R.id.action_mainFragment_to_loginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


