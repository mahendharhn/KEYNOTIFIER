package com.dimp.keynotifier

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_login)


        val signup_button: TextView =findViewById(R.id.login_signup_button)


        signup_button.setOnClickListener{
            val intent= Intent(this,SignupActivity::class.java)
            startActivity(intent)
            finish()
        }


        val login_button:TextView=findViewById(R.id.login_button)

        login_button.setOnClickListener{

            perforemregister()
        }



    }

    private fun perforemregister() {
        val email=login_email.text.toString()
        val password=login_password.text.toString()
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please fill out email", Toast.LENGTH_SHORT).show()
            return
        }else if (!isValidString(email)){
            Toast.makeText(this,"Invalid email", Toast.LENGTH_SHORT).show()
            return
        }else{
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener

                    Log.d("login,","Login Successfull")
                    Toast.makeText(this,"Login sucessfull", Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this,"failed to log in", Toast.LENGTH_SHORT).show()
                }

        }
    }

    fun isValidString(str: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }
}