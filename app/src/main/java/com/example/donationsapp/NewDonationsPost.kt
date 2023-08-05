package com.example.donationsapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.donationsapp.databinding.ActivityNewDonationsPostBinding
import com.example.donationsapp.datasource.DataClassDonation
import com.example.donationsapp.datasource.User
import com.example.donationsapp.ui.donations.DonationsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.util.Calendar

class NewDonationsPost : AppCompatActivity() {

    private lateinit var binding: ActivityNewDonationsPostBinding
    var imageURL: String? = null
    private var uri: Uri? = null
    private lateinit var user: User
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewDonationsPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                uri = data!!.data
                binding.uploadImage.setImageURI(uri)
            } else {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.selectButton.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        binding.uploadButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            saveData()
            finish()
        }

    }

    private fun saveData() {
        val storageReference = FirebaseStorage.getInstance().reference.child("Donation Post Images")
            .child(uri!!.lastPathSegment!!)
        storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageURL = urlImage.toString()
            uploadData()
        }.addOnFailureListener {
            Toast.makeText(
                this@NewDonationsPost, "save data failed", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun uploadData() {
        val caption = binding.postCaption.text.toString()
        val title = binding.postTitle.text.toString()
        val type = if (binding.foodPost.isChecked) {
            "Food"
        } else if (binding.clothingPost.isChecked) {
            "Clothing"
        } else if (binding.housewarePost.isChecked) {
            "Houseware"
        } else if (binding.volunteeringPost.isChecked) {
            "Volunteering"
        } else if (binding.techPost.isChecked) {
            "Technology"
        } else {
            "Miscellaneous"
        }

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val userId = firebaseAuth.currentUser?.uid
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        if (userId.toString().isNotEmpty()) {
            databaseReference.child(userId.toString()).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)!!
                    val dataClass =
                        DataClassDonation(title, type, caption, user.organization, imageURL)
                    FirebaseDatabase.getInstance().getReference("Donation Posts").child(currentDate)
                        .setValue(dataClass).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@NewDonationsPost,
                                    "Uploaded Successfully!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                binding.postCaption.setText("")
                                binding.progressBar.visibility = View.GONE
                            }
                        }.addOnFailureListener {
                            Toast.makeText(
                                this@NewDonationsPost, "upload data failed", Toast.LENGTH_SHORT
                            ).show()
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@NewDonationsPost,
                        "Failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

}