package com.example.donationsapp.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.donationsapp.datasource.User
import com.example.donationsapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: User

    //connects fragment_profile.xml to this vv
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val userId = firebaseAuth.currentUser?.uid

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (firebaseAuth.currentUser != null) {
            firebaseAuth.currentUser?.let {
                binding.userEmail.text = it.email
            }
        }

        binding.saveButton.setOnClickListener {
            val firstName = binding.firstNameInput.text.toString()
            val lastName = binding.lastNameInput.text.toString()
            val organization = binding.organizationInput.text.toString()

            if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                val user = User(firstName, lastName, organization)
                if (userId != null) {
                    databaseReference.child(userId).setValue(user).addOnCompleteListener {
                        activity?.finish()
                        Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
                    }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "Failed!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            } else {
                Toast.makeText(requireContext(), "Fill in all spaces.", Toast.LENGTH_SHORT).show()
            }
        }

        //displays first name, lastname, and status in my profile
        if (userId.toString().isNotEmpty()) {
            databaseReference.child(userId.toString())
                .addValueEventListener(object : ValueEventListener {
                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        user = snapshot.getValue(User::class.java)!!
                        binding.fullName.text =
                            user.firstName + " " + user.lastName
                        binding.organizationDisplayProfile.text = user.organization
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            requireContext(),
                            "Failed!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}