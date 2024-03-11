package com.example.postest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InventoryFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var inventoryAdapter: InventoryAdapter
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)

        searchView = view.findViewById(R.id.searchView2)
        recyclerView = view.findViewById(R.id.inventoryRecyclerView)
        val addItemButton: Button = view.findViewById(R.id.addItemButton)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize database reference
        databaseRef = FirebaseDatabase.getInstance().reference.child("inventory")

        // Set up adapter
        inventoryAdapter = InventoryAdapter(ArrayList())
        recyclerView.adapter = inventoryAdapter

        // Fetch data from Firebase and set up adapter
        fetchInventoryDataFromFirebase()

        // Set up "Add Item" button click listener
        addItemButton.setOnClickListener {
            val addItemFormFragment = AddItemFormFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, addItemFormFragment)
                .addToBackStack(null)
                .commit()
        }

        // Set up search view
        setUpSearchView()

        return view
    }

    private fun fetchInventoryDataFromFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val inventoryItemList = mutableListOf<InventoryItem>()
                for (itemSnapshot in snapshot.children) {
                    val itemId = itemSnapshot.key ?: ""
                    val itemName = itemSnapshot.child("itemName").getValue(String::class.java) ?: ""
                    val itemQuantity = itemSnapshot.child("itemQuantity").getValue(Int::class.java) ?: 0
                    val itemPrice = itemSnapshot.child("itemPrice").getValue(Double::class.java) ?: 0.0

                    val inventoryItem = InventoryItem(itemId, itemName, itemQuantity, itemPrice)
                    inventoryItemList.add(inventoryItem)
                }
                inventoryAdapter.setInventoryList(inventoryItemList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun setUpSearchView() {
        // Set up SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search when user submits query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search as user types
                // Example: Filter your data based on newText
                // and update RecyclerView accordingly
                return true
            }
        })
    }

    // This function should be inside the class
    fun selectImageFromGallery(activity: Activity, requestCode: Int) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, requestCode)
    }
}
