package com.example.newsapi.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.newsapi.Models.Users
import com.example.newsapi.R
import com.example.newsapi.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var rootNode: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater)
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
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode.getReference("Users")
        auth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)

        binding.signUpButton.setOnClickListener() {
            binding.signUpButton.visibility = Button.GONE
            val email: String = binding.etEmail.text.toString().trim()
            val passw: String = binding.etPassword.text.toString().trim()
            val cnfpassw: String = binding.etCnfrmpasswrd.text.toString().trim()
            val age: String = binding.etAge.text.toString().trim()
            val phone: String = binding.etPhone.text.toString().trim()
            val address: String = binding.etAddress.text.toString().trim()
            val bio: String = binding.etBio.text.toString().trim()
            var userId: String = ""
            if (email.isEmpty() || passw.isEmpty() || cnfpassw.isEmpty()) {
                binding.signUpButton.visibility = Button.VISIBLE
                showToast("Email and password is mandatory.")
            } else if (!checkemail(email)) {
                binding.signUpButton.visibility = Button.VISIBLE
                showToast("Given email is invalid.")
            } else if (!validatepass(passw)) {
                binding.signUpButton.visibility = Button.VISIBLE
                showToast("Password must contain a digit, a capital letter and should be of length 8-20.")
            } else if (!validatephone(phone)) {
                binding.signUpButton.visibility = Button.VISIBLE
                showToast("Please enter a valid phone number!")
            } else if (!validateage(age)) {
                binding.signUpButton.visibility = Button.VISIBLE
                showToast("Please enter a valid age!")
            } else if (!(passw.compareTo(cnfpassw) == 0)) {
                binding.signUpButton.visibility = Button.VISIBLE
                showToast("Passwords doesn't match! Please try again.")
            } else {
                val user = Users(email, passw, cnfpassw, age, phone, address, bio)

                auth.createUserWithEmailAndPassword(email, passw)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            userId = auth.currentUser!!.uid
                            reference.child(userId).setValue(user)
                            navController.navigate(R.id.action_signInFragment_to_loginFragment)
                        } else {
                            binding.signUpButton.visibility = Button.VISIBLE
                            val exc: String = task.exception.toString()
                            val ind: Int = exc.indexOf(':') + 2
                            showToast(exc.substring(ind))
                            Log.d("error", "error in creation", task.exception)
                        }
                    }
            }
        }

        binding.tvTextclickable.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_loginFragment)
        }
    }


    fun showToast(string: String) {
        Toast.makeText(this.context, string, Toast.LENGTH_SHORT).show()
    }
    fun validateage(age: String): Boolean{
        return try {
            var a_ge =  Integer.valueOf(age)
            a_ge in 0..120
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun validatepass(pass: String): Boolean {
        var regex = "^(?=.*[0-9])(?=.*[A-Z]).{8,20}$"
        var p: Pattern = Pattern.compile(regex)
        if (pass.isEmpty()) return false
        var m = p.matcher(pass)
        return m.matches()
    }

    fun validatephone(phone: String): Boolean {
        return if (TextUtils.isEmpty(phone)) {
            false
        } else {
            Patterns.PHONE.matcher(phone).matches()
        }
    }

    fun checkemail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
}