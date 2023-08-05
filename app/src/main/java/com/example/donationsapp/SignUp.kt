package com.example.donationsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.donationsapp.databinding.SignupActivityBinding
import com.example.donationsapp.datasource.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUp : AppCompatActivity() {

    private lateinit var binding: SignupActivityBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        binding.regButton.setOnClickListener{
            val pass = binding.signupPass.text.toString()
            val username = binding.signupUser.text.toString()
            val confPass = binding.confPass.text.toString()
            val firstName = binding.firstname.text.toString()
            val lastName = binding.lastname.text.toString()
            val organization = binding.organization.text.toString()

            if (pass.isNotEmpty() && username.isNotEmpty() && confPass.isNotEmpty()) {
                if (confPass == pass) {
                    firebaseAuth.createUserWithEmailAndPassword(username, pass)
                        .addOnCompleteListener { it ->
                            if (it.isSuccessful) {
                                firebaseAuth.signInWithEmailAndPassword(username, pass)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val userId = firebaseAuth.currentUser?.uid
                                            user = User(firstName, lastName, organization)
                                            if (userId != null) {
                                                databaseReference.child(userId).setValue(user)
                                                    .addOnCompleteListener {
                                                        Toast.makeText(
                                                            this,
                                                            "Success!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(
                                                            this,
                                                            "Failed!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                            }
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "user not created",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    }
                            } else {
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill in all spaces.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backLogin.setOnClickListener{
            //Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }

    }
}
