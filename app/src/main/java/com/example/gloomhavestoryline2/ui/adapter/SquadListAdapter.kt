package com.example.gloomhavestoryline2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gloomhavestoryline2.databinding.MemberViewBinding
import com.example.gloomhavestoryline2.db.entities.Character

class SquadListAdapter(private var squad: List<Character>): RecyclerView.Adapter<SquadListAdapter.MemberViewHolder>() {

    class MemberViewHolder private constructor(val binding: MemberViewBinding): RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): MemberViewHolder {
                val layout = LayoutInflater.from(parent.context)
                val binding = MemberViewBinding.inflate(layout, parent, false)
                return MemberViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return squad.size
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.binding.character = squad[position]
    }

    fun updateList(newList: List<Character>) {
        squad = newList
        notifyDataSetChanged()
    }
}