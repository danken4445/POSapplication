package com.example.postest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReceiptAdapter : RecyclerView.Adapter<ReceiptAdapter.ReceiptItemViewHolder>() {

    private var itemList: List<ReceiptItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_receipt, parent, false)
        return ReceiptItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceiptItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun submitList(newList: List<ReceiptItem>) {
        itemList = newList
        notifyDataSetChanged()
    }

    inner class ReceiptItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        private val itemPriceTextView: TextView = itemView.findViewById(R.id.itemPriceTextView)

        fun bind(item: ReceiptItem) {
            itemNameTextView.text = item.name
            itemPriceTextView.text = item.price.toString()
        }
    }
}
