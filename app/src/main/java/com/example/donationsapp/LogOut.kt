package com.example.donationsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.donationsapp.databinding.ActivityLogOutBinding
import com.google.firebase.auth.FirebaseAuth

private lateinit var binding: ActivityLogOutBinding
private lateinit var firebaseAuth: FirebaseAuth

class LogOut : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()
        val intent = Intent(this, LogIn::class.java)
        startActivity(intent)

        finish()

    }
}