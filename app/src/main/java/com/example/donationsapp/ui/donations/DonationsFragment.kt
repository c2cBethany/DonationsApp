package com.example.donationsapp.ui.donations

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.donationsapp.NewDonationsPost
import com.example.donationsapp.datasource.DataClassDonation
import com.example.donationsapp.adaptors.ImagesAdapter
import com.example.donationsapp.databinding.FragmentDonationsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class DonationsFragment : Fragment() {

    private var _binding: FragmentDonationsBinding? = null
    var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null
    private lateinit var dataList: ArrayList<DataClassDonation>
    private lateinit var adapter: ImagesAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDonationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabDonationsPost.setOnClickListener {
            val intent = Intent(activity, NewDonationsPost::class.java)
            startActivity(intent)
        }

        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.layoutManager = gridLayoutManager

        binding.progressBar.visibility = View.VISIBLE
        dataList = ArrayList()
        adapter = ImagesAdapter(requireContext(), dataList)
        binding.recyclerView.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().getReference("Donation Posts")
        binding.progressBar.visibility = View.GONE

        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataClassDonation::class.java)
                    if (dataClass != null) {
                        dataList.add(0, dataClass)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(), "Database error", Toast.LENGTH_SHORT
                ).show()
            }
        })

        binding.search.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })

        return root
    }

    fun searchList(text: String) {
        val searchList = java.util.ArrayList<DataClassDonation>()
        for (dataClass in dataList) {
            if (dataClass.dataOrganization?.lowercase()
                    ?.contains(text.lowercase(Locale.getDefault())) == true
            ) {
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dataList.clear()
    }
}