package com.example.gloomhavestoryline2.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.ItemViewBinding
import com.example.gloomhavestoryline2.db.entities.Item
import com.example.gloomhavestoryline2.db.repository.StorageRepository

class ItemListAdapter(private var items: List<Item>,private val context: Context, private val onClick: (Item) -> Unit): RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {

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
        val image = StorageRepository.downloadItemImage(items[position].image)
        val TAG = "Item_LIST_ADAPTER"
        Log.d(TAG,"${items[position].image}")
        Log.d(TAG,"image: $image")
        Glide.with(context)
            .load(image)
            .error(R.drawable.default_item)
            .into(holder.binding.itemImageView)
    }

    fun updateList(newList: List<Item>) {
        items = newList
        notifyDataSetChanged()
    }
}