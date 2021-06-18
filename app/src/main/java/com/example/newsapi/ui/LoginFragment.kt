package com.example.newsapi.ui

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.newsapi.R
import com.example.newsapi.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

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

        if (auth.currentUser != null) {
            navController.navigate(R.id.action_loginFragment_to_mainFragment)
        } else {
            binding.loginButton.setOnClickListener() {
                binding.loginButton.visibility = Button.GONE
                closeKeyboard()
                val email: String = binding.etEmail.text.toString().trim()
                val passw: String = binding.etPassword.text.toString().trim()

                if (checkemail(email) && validatepass(passw))
                    auth.signInWithEmailAndPassword(email, passw).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.navigate(R.id.action_loginFragment_to_mainFragment)
                        } else {
                            binding.loginButton.visibility = Button.VISIBLE
                            showToast("Authentication Error!")
                        }
                    }
                else{
                    showToast("Wrong credentials!")
                    binding.loginButton.visibility = Button.VISIBLE
                }
            }
        }

        binding.tvTextclickable.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signInFragment)
        }
    }

    private fun closeKeyboard() {
        val inputManager: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            requireActivity().currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun showToast(string: String) {
        Toast.makeText(this.context, string, Toast.LENGTH_SHORT).show()
    }

    fun validatepass(pass: String): Boolean {
        var regex = "^(?=.*[0-9])(?=.*[A-Z]).{8,20}$"
        var p: Pattern = Pattern.compile(regex)
        if (pass.isEmpty()) return false
        var m = p.matcher(pass)
        return m.matches()
    }


    fun checkemail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
}