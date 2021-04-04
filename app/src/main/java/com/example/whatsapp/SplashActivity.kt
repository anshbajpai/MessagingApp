package com.example.whatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    val auth by lazy{
        FirebaseAuth.getInstance()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(auth.currentUser == null){

            Handler().postDelayed({
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            },400)

        }
        else{
            Handler().postDelayed(
                {
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                },400)

        }
    }
}