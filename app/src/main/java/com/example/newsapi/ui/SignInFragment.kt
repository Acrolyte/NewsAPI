package com.example.newsapi.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
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
    private lateinit var auth : FirebaseAuth
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

            val email : String = binding.etEmail.text.toString().trim()
            val passw : String = binding.etPassword.text.toString().trim()
            val cnfpassw : String = binding.etCnfrmpasswrd.text.toString().trim()
            val age : String = binding.etAge.text.toString().trim()
            val phone : String = binding.etPhone.text.toString().trim()
            val address : String = binding.etAddress.text.toString().trim()
            val bio : String = binding.etBio.text.toString().trim()
            var userId : String = ""
            if(email.isEmpty()||passw.isEmpty()||cnfpassw.isEmpty()){
                Toast.makeText(this.context,"Email and password is mandatory.",Toast.LENGTH_SHORT).show()
            }else {
                if (email.isNotEmpty()) {
                    userId = email.substring(0, email.indexOf('@'))
                }
                if(!validatepass(passw))
                    Toast.makeText(this.context,"Password must contain a digit, a capital letter and should be of length 8-20.",Toast.LENGTH_LONG).show()
                else {
//            Log.d("values","$email $passw $cnfpassw $age $phone $address $bio")

                    if (passw.compareTo(cnfpassw) == 0) {
                        val user = Users(email, passw, cnfpassw, age, phone, address, bio)
                        reference.child(userId).setValue(user).addOnCompleteListener {
                            if (it.isSuccessful) {
                                auth.createUserWithEmailAndPassword(email, passw)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            navController.navigate(R.id.action_signInFragment_to_loginFragment)
                                        } else {
                                            val exc: String = task.exception.toString()
                                            val ind: Int = exc.indexOf(':') + 2
                                            Toast.makeText(
                                                this.context,
                                                exc.substring(ind),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            Log.d("error", "error in creation", task.exception)
                                        }
                                    }
                            }
                        }
                    } else {
                        Toast.makeText(
                            this.context,
                            "Passwords doesn't match! Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }

        binding.tvTextclickable.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_loginFragment)
        }
    }
    fun validatepass(pass: String) : Boolean{
        var regex = "^(?=.*[0-9])(?=.*[A-Z]).{8,20}$"
        var p : Pattern = Pattern.compile(regex)
        if(pass.isEmpty()) return false
        var m = p.matcher(pass)
        return m.matches()
    }
}