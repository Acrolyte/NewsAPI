package com.example.newsapi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.newsapi.R
import com.example.newsapi.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)

        binding.loginButton.setOnClickListener() {
            val email: String = binding.etEmail.text.toString().trim()
            val passw: String = binding.etPassword.text.toString().trim()

            auth.signInWithEmailAndPassword(email, passw).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    navController.navigate(R.id.action_loginFragment_to_mainFragment)
                }else{
                    Toast.makeText(this.context,"Authentication Error!",Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvTextclickable.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signInFragment)
        }
    }
}