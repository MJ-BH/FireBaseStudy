package com.example.firebasestudy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasestudy.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View? = binding.root
        setContentView(view)
        delay()
    }

    fun delay() {
        val handler: Handler? = Handler()
        // Do something after 3s = 3000ms
        handler?.postDelayed({ isConnected() }, 3000)
    }

    private fun isConnected() {
        mAuth = FirebaseAuth.getInstance()
        if (mAuth?.currentUser != null) {
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            finish()
        }
    }
}