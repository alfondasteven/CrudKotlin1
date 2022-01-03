package com.fonda.crudkotlin1

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.fonda.crudkotlin1.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    //viewBinding
    private lateinit var binding: ActivitySignUpBinding
    //ActionBar
    private lateinit var actionBar: ActionBar
    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //config actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //config proges dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Creating Account in...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //handle click , signup
        binding.signUpBtn.setOnClickListener {
            //validate  data
            validateData()
        }
    }

    private fun validateData() {
        //get data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email
            binding.emailEt.error = "Invalid email format"
        }
        else if(TextUtils.isEmpty(password)){
            //password g diisi
            binding.passwordEt.error = "Please enter password"
        }
        else if(password.length < 6){
            binding.passwordEt.error = "Password must atleast 6 character"
        }
        else{
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        //show proges
        progressDialog.show()

        //create acc
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //signup succes
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"Account created with email $email",Toast.LENGTH_SHORT).show()

                //open profil
                startActivity(Intent(this,ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener{e->
                //sign up failed
                progressDialog.dismiss()
                Toast.makeText(this,"SignUp failed due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}