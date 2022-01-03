package com.fonda.crudkotlin1

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.fonda.crudkotlin1.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding:ActivityLoginBinding

    //ActionBar
    private lateinit var actionBar: ActionBar
    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog
    //FirebaseAuth
    private lateinit var firebaseAuth : FirebaseAuth
    private var email =""
    private var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser();

        //open register activity
        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        //awalan login
        binding.loginBtn.setOnClickListener {
            validateData()
        }

    }

    private fun validateData() {
        //GET DATA
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid
            binding.emailEt.error = "Invalid email format"
        }
        else if(TextUtils.isEmpty(password)){
            binding.passwordEt.error = "Please enter password"
        }
        else{
            //data bener
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //Login succes
                progressDialog.dismiss()
                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"Logged as $email",Toast.LENGTH_SHORT).show()

                //open profile
                startActivity(Intent(this,ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener {e->
                //Login fail
                progressDialog.dismiss()
                Toast.makeText(this,"Login failed due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }
}