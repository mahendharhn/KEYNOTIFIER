package com.dimp.keynotifier

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.mac_layout.view.*

private val TAG="SignupActivity"
class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_signup)
        val login_button:TextView=findViewById(R.id.signup_login_button)

        login_button.setOnClickListener {

            val intent=Intent(this,LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            }


        auth = Firebase.auth
        
        val signup_button: TextView = findViewById(R.id.signup_button)
        signup_button.setOnClickListener{
            val signup_password:TextView=findViewById(R.id.signup_password)
            val signup_username:TextView=findViewById(R.id.signup_username)
            val signup_email:TextView=findViewById(R.id.signup_email)
            val email=signup_email.text.toString()

            
            val password=signup_password.text.toString()
            val username=signup_username.text.toString()
            Log.d(TAG, "USERNAME=$username\n EMAIL=$email\n Password=$password")

            performregister(email, password,username)


        }
    }

    private fun performregister(email: String, password: String,username:String){
        if(password.length<6){
            Toast.makeText(baseContext,"Password length should be atleast 6 ",Toast.LENGTH_SHORT).show()
        }else if(!isValidString(email)){
            Log.d(TAG,"Invalid email")
            Toast.makeText(baseContext,"Invalid email",Toast.LENGTH_SHORT).show()

        }else if(username.length<=0){
            Toast.makeText(this,"Username empty",Toast.LENGTH_SHORT).show()
        }else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        Toast.makeText(
                            baseContext, "Sign Up successfull.",
                            Toast.LENGTH_SHORT
                        ).show()

                        uploadImagetoFireBaseDatabase(email, username)

                        val mDialogView= LayoutInflater.from(this).inflate(R.layout.mac_layout,null)
                        val mBuilder= AlertDialog.Builder(this).setView(mDialogView).setTitle(" ")
//                        mBuilder.show()

                        val mAlertDialog=mBuilder.show()

                        mDialogView.save_mac_button.setOnClickListener{
                            val uid=FirebaseAuth.getInstance().uid?:""
                            val ref= FirebaseDatabase.getInstance().getReference("/users/$uid/macAddress")
                            val macString:TextView=mDialogView.mac_text_view
                            val macAdd=mDialogView.mac_text_view.text.toString()
                            if(macAdd.isEmpty()){
                                Toast.makeText(this,"MacAddress length is 0",Toast.LENGTH_SHORT).show()
                                macString.setText("")
                            }else if(macAdd.length<12){
                                Toast.makeText(this,"MacAddress length should be minimum 12",Toast.LENGTH_SHORT).show()
                                macString.setText("")
                            }else if (macAdd.length>12){
                                Toast.makeText(this,"MacAddress length should be maximum 12",Toast.LENGTH_SHORT).show()
                                macString.setText("")
                            }else if(macAdd.length==12){
                                Toast.makeText(this,"Saving...",Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                ref.setValue(macAdd)

                            }


                        }


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    // ...
                }

        }


    }

    fun isValidString(str: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    private fun uploadImagetoFireBaseDatabase(email: String, username: String) {
        val uid=FirebaseAuth.getInstance().uid?:""
        val ref= FirebaseDatabase.getInstance().getReference("/users/$uid")
        val mac="12345678"
        val user=User(
            uid,
            username,
            mac,
            email
        )

        ref.setValue(user).addOnSuccessListener {
            Log.d(TAG, "Finally the user is saved to database")
        }.addOnFailureListener{
            Log.d(TAG,"Failed ${it.message}")
        }

    }


}