package com.example.newsapi.ui

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
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

        var email: String = binding.etEmail.text.toString().trim()
        var passw: String = binding.etPassword.text.toString().trim()
        var cnfpassw: String = binding.etCnfrmpasswrd.text.toString().trim()
        var age: String = binding.etAge.text.toString().trim()
        var phone: String = binding.etPhone.text.toString().trim()
        var address: String = binding.etAddress.text.toString().trim()
        var bio: String = binding.etBio.text.toString().trim()
        var userId: String = ""

        binding.etEmail.doOnTextChanged { text, start, before, count ->
            if (!checkemail(text))
                binding.til1.error = "Given email is invalid."
            else{
                binding.til1.error = null
            }
        }

        binding.etPassword.doOnTextChanged { text, start, before, count ->
            if (!validatepass(text.toString()))
                binding.til2.error =
                    "Password must contain a digit, a capital letter and should be of length 8-20."
            else{
                binding.til2.error = null
            }
        }
        binding.etCnfrmpasswrd.doOnTextChanged { text, start, before, count ->
            if (!(binding.etPassword.text.toString().compareTo(text.toString()) == 0))
                binding.til3.error = "Passwords doesn't match! Please try again."
            else {
                binding.til3.error = null
            }
        }
        binding.etPhone.doOnTextChanged { text, start, before, count ->
            if (!validatephone(text.toString()))
                binding.til5.error = "Please enter a valid phone number!"
            else {
                binding.til5.error = null
            }
        }

        binding.etAge.doOnTextChanged { text, start, before, count ->
            if (!validateage(text.toString()))
                binding.til4.error = "Please enter a valid age!"
            else {
                binding.til4.error = null
            }
        }

        binding.signUpButton.setOnClickListener() {
            binding.signUpButton.visibility = Button.GONE
            closeKeyboard()
//            if (email.isEmpty()) {
//                binding.til1.error = "Field empty!"
//                binding.signUpButton.visibility = Button.VISIBLE
//            }
//            if (passw.isEmpty()) {
//                binding.til2.error = "Field empty!"
//                binding.signUpButton.visibility = Button.VISIBLE
//            }
//            if (cnfpassw.isEmpty()){
//                binding.til3.error = "Field empty!"
//                binding.signUpButton.visibility = Button.VISIBLE
//            }

            email= binding.etEmail.text.toString().trim()
            passw= binding.etPassword.text.toString().trim()
            cnfpassw = binding.etCnfrmpasswrd.text.toString().trim()
            age = binding.etAge.text.toString().trim()
            phone= binding.etPhone.text.toString().trim()
            address = binding.etAddress.text.toString().trim()
            bio = binding.etBio.text.toString().trim()
            if (validatepass(passw) && validateage(age) && validatephone(phone) && checkemail(email)) {
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
            else{
                showToast("Something's wrong")
                binding.signUpButton.visibility = Button.VISIBLE
            }
        }

        binding.tvTextclickable.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_loginFragment)
        }
    }

    private fun closeKeyboard() {
        val inputManager: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            requireActivity().currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }


    fun showToast(string: String) {
        Toast.makeText(this.context, string, Toast.LENGTH_SHORT).show()
    }

    fun validateage(age: String): Boolean {
        return try {
            var a_ge = Integer.valueOf(age)
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