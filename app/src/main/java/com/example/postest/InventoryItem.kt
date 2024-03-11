package com.example.postest

data class InventoryItem(
    val itemId: String,
    val itemName: String,
    val itemQuantity: Int,
    val itemPrice: Double,
    val imageResource: String? = null
    // Add other item details as needed
) {
}

