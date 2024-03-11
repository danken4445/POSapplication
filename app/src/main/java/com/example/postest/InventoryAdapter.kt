package com.example.postest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference

import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import java.util.Locale

class InventoryAdapter(private var itemList: List<InventoryItem>) :
    RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>(), Filterable {

    // Add a mutable list to hold the original unfiltered data
    private var originalItemList: List<InventoryItem> = ArrayList(itemList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventory, parent, false)
        return InventoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = itemList.size

    // Update the dataset with new data
    fun setInventoryList(inventoryItemList: List<InventoryItem>) {
        itemList = inventoryItemList
        originalItemList = ArrayList(inventoryItemList) // Update original list as well
        notifyDataSetChanged()
    }

    // Implement Filterable interface methods
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<InventoryItem>()
                val query = constraint.toString().toLowerCase(Locale.ROOT).trim()

                if (query.isEmpty()) {
                    filteredList.addAll(originalItemList)
                } else {
                    for (item in originalItemList) {
                        if (item.itemName.toLowerCase(Locale.ROOT).contains(query)) {
                            filteredList.add(item)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemList = results?.values as List<InventoryItem>
                notifyDataSetChanged()
            }
        }
    }

    inner class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        private val itemPriceTextView: TextView = itemView.findViewById(R.id.itemPriceTextView)
        private val itemQuantityTextView: TextView =
            itemView.findViewById(R.id.itemQuantityTextView)
        private val itemImageView: ImageView = itemView.findViewById(R.id.itemImageView)
        private val increaseQuantityButton: ImageButton =
            itemView.findViewById(R.id.increaseQuantityButton)
        private val decreaseQuantityButton: ImageButton =
            itemView.findViewById(R.id.decreaseQuantityButton)

        fun bind(inventoryItem: InventoryItem) {
            // Bind data to views
            itemNameTextView.text = inventoryItem.itemName
            itemPriceTextView.text = "Price: ₱${inventoryItem.itemPrice}"
            itemQuantityTextView.text = "Quantity: ${inventoryItem.itemQuantity}"
            // Set image resource (you may need to load the image asynchronously)
            // itemImageView.setImageResource(inventoryItem.imageResource)

            // Example for loading image asynchronously (if needed)
            // Glide.with(itemView.context).load(inventoryItem.imageResource).into(itemImageView)

            // Set click listeners for quantity buttons if needed
            increaseQuantityButton.setOnClickListener {
                // Increase the quantity of the item
                val updatedQuantity = inventoryItem.itemQuantity + 1
                itemQuantityTextView.text = "Quantity: $updatedQuantity"
                // Update the quantity in the data source if needed
                // Example: Update Firebase with the new quantity
            }

            decreaseQuantityButton.setOnClickListener {
                // Decrease the quantity of the item, if it's greater than 0
                if (inventoryItem.itemQuantity > 0) {
                    val updatedQuantity = inventoryItem.itemQuantity - 1
                    itemQuantityTextView.text = "Quantity: $updatedQuantity"
                    // Update the quantity in the data source if needed
                    // Example: Update Firebase with the new quantity
                } else {
                    // Handle case where quantity is already 0
                    Toast.makeText(
                        itemView.context,
                        "Quantity cannot be less than 0",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}