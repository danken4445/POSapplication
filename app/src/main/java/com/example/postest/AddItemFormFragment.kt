package com.example.postest

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddItemFormFragment : Fragment() {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var itemNameEditText: EditText
    private lateinit var itemPriceEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var selectImageButton: Button
    private var itemIdCounter: Int = 0

    private val REQUEST_CODE_GALLERY = 123

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_item_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize database reference
        databaseRef = FirebaseDatabase.getInstance().reference.child("Items")

        // Initialize views
        itemNameEditText = view.findViewById(R.id.itemNameEditText)
        itemPriceEditText = view.findViewById(R.id.itemPriceEditText)
        quantityEditText = view.findViewById(R.id.quantityEditText)
        selectImageButton = view.findViewById(R.id.selectImageButton)
        val addButton = view.findViewById<Button>(R.id.addButton)

        // Set click listener for image selection button
        selectImageButton.setOnClickListener {
            selectImageFromGallery()
        }

        // Retrieve last used item ID from the database
        databaseRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (itemSnapshot in dataSnapshot.children) {
                    // Update itemIdCounter to one greater than the last used item ID
                    itemIdCounter = itemSnapshot.key?.toIntOrNull()?.plus(1) ?: 0
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

        // Set up button click listener
        addButton.setOnClickListener {
            addItemToDatabase()
        }
    }

    private fun selectImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY)
    }
    companion object {
        private const val REQUEST_CODE_GALLERY = 123
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            // Handle the selected image URI here
            val selectedImageUri = data.data
            // You can set the selected image URI to a ImageView or process it as needed
        }
    }
    private fun addItemToDatabase() {
        val itemName = itemNameEditText.text.toString().trim()
        val itemPrice = itemPriceEditText.text.toString().trim()
        val quantity = quantityEditText.text.toString().toIntOrNull() ?: 0
        val imageResource = selectImageButton.text.toString().trim()

        if (itemName.isNotEmpty() && quantity > 0 && imageResource.isNotEmpty()) {
            // Generate unique key for the new item
            val newItemId = itemIdCounter.toString()

            // Create map with item details
            val itemMap = mapOf(
                "itemName" to itemName,
                "itemQuantity" to quantity,
                "imageResource" to imageResource,
                "itemPrice" to itemPrice
                // Add other item details here if needed
            )

            // Add item to the database under the generated ID
            databaseRef.child(newItemId).setValue(itemMap)
                .addOnSuccessListener {
                    // Increment the item ID counter
                    itemIdCounter++
                    // Show confirmation dialog
                    showConfirmationDialog()
                }
                .addOnFailureListener {
                    // Error occurred while adding item to database
                    // Handle error, display message, etc.
                    Toast.makeText(requireContext(), "Failed to add item", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Show error message if any field is empty or invalid
            Toast.makeText(requireContext(), "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Item Added, Do you want to add another item?")
            .setPositiveButton("Yes") { _, _ ->
                // Clear input fields for new input
                itemNameEditText.text.clear()
                quantityEditText.text.clear()
                itemPriceEditText.text.clear()
            }
            .setNegativeButton("No") { _, _ ->
                // Navigate back to the inventory fragment
                findNavController().popBackStack()
            }
            .setCancelable(false)
            .show()
    }
}
