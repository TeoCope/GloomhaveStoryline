package com.example.gloomhavestoryline2.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.ItemCharacterViewBinding
import com.example.gloomhavestoryline2.db.entities.Item
import com.example.gloomhavestoryline2.db.repository.StorageRepository

class ItemCharacterListAdapter(private var items: List<Item>,private val context: Context, private val onCLick:(Item) -> Unit): RecyclerView.Adapter<ItemCharacterListAdapter.ItemViewHolder>() {

    class ItemViewHolder private constructor(val binding: ItemCharacterViewBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ItemViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCharacterViewBinding.inflate(layoutInflater, parent, false)
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
        holder.binding.itemWrapper.setOnClickListener {
            onCLick(items[position])
        }
        val image = StorageRepository.downloadItemImage(items[position].image)
        Glide.with(context)
            .load(image)
            .error(R.drawable.default_item)
            .into(holder.binding.imageView)
    }

    fun updateAdapter(newList: List<Item>) {
        items = newList
        notifyDataSetChanged()
    }
}