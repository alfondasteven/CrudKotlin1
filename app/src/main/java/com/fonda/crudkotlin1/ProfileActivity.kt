package com.fonda.crudkotlin1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.fonda.crudkotlin1.databinding.ActivityProfileBinding
import com.fonda.crudkotlin1.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    //viewbinding
    private lateinit var binding: ActivityProfileBinding
    //actionbar
    private lateinit var actionBar : ActionBar
    //firebaseauth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Profile"

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
    }

    private fun checkUser() {
        //check user udah logini blm
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user not null , user udh login , dpt user info
            val email = firebaseUser.email
            binding.emailTv.text = email
        }
        else{
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}