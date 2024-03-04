package com.example.gloomhavestoryline2.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.AbilityViewBinding
import com.example.gloomhavestoryline2.databinding.GameViewBinding
import com.example.gloomhavestoryline2.db.entities.Ability
import com.example.gloomhavestoryline2.db.repository.StorageRepository

class AbilitiesListAdapter(private var abilities: List<Ability>,private val context: Context): RecyclerView.Adapter<AbilitiesListAdapter.AbilityViewHolder>() {

    class AbilityViewHolder private constructor(val binding: AbilityViewBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): AbilityViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AbilityViewBinding.inflate(layoutInflater,parent,false)
                return AbilityViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbilityViewHolder {
        return AbilityViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return abilities.size
    }

    override fun onBindViewHolder(holder: AbilityViewHolder, position: Int) {
        holder.binding.ability = abilities[position]
        Glide.with(context)
            .load(StorageRepository.downloadAbilityImage(abilities[position].image))
            .error(R.drawable.img)
            .into(holder.binding.imageView)
    }

    fun updateAdapter(newList: List<Ability>) {
        abilities = newList
        notifyDataSetChanged()
    }
}