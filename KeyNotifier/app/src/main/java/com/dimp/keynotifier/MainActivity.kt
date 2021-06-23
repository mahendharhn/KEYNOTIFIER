package com.dimp.keynotifier

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var hello = findViewById<Button>(R.id.btnStart)
        var hello1 = findViewById<TextView>(R.id.tv1)
        var hello2 = findViewById<Button>(R.id.btn2Start)
        hello.setOnClickListener {
            Intent(this, MyService::class.java).also {
                startService(it)
                hello1.text = "Key Notifier is ON"
            }
        }

        hello2.setOnClickListener {
            Intent(this, MyService::class.java).also {

                MyService.stopService()
                hello1.text = "Key Notifier is OFF"

            }

        }


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_sign_out->{
                FirebaseAuth.getInstance().signOut()
                MyService.stopService()
                val intent=Intent(this,LoginActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }



        }
        return super.onOptionsItemSelected(item)

    }



}