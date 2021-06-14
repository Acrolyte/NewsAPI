package com.example.newsapi.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.newsapi.R
import com.example.newsapi.databinding.FragmentDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var rootnode: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

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
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        rootnode = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser!!
        val email = user.email!!
        val userId : String = auth.currentUser!!.uid
        reference = rootnode.getReference("Users").child(userId)

        reference.get().addOnSuccessListener {
            binding.pbCurve.visibility = ProgressBar.GONE
            var useremail : String = it.child("email").value.toString()
            var userage : String = it.child("age").value.toString()
            var useraddress : String = it.child("address").value.toString()
            var userphone : String = it.child("phone").value.toString()
            var userbio : String = it.child("bio").value.toString()

            binding.etEmail.text = useremail
            binding.etAge.text = userage
            binding.etAddress.text = useraddress
            binding.etPhone.text = userphone
            binding.etBio.text = userbio

            Log.d("snapshot",it.value.toString())
        }
        binding.logoutButton.setOnClickListener{
            auth.signOut()
            navController.navigate(R.id.action_detailsFragment_to_loginFragment)
        }
    }
}