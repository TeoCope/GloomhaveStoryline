package com.example.gloomhavestoryline2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gloomhavestoryline2.databinding.FragmentStorylineBinding
import com.example.gloomhavestoryline2.databinding.ItemViewBinding
import com.example.gloomhavestoryline2.db.entities.Item

class ItemListAdapter(private var items: List<Item>, private val onClick: (Item) -> Unit): RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {

    class ItemViewHolder private constructor(val binding: ItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemViewBinding.inflate(layoutInflater,parent,false)
                return ItemViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.item = items[position]
        holder.binding.itemCard.setOnClickListener {
            onClick(items[position])
        }
    }

    fun updateList(newList: List<Item>) {
        items = newList
        notifyDataSetChanged()
    }
}