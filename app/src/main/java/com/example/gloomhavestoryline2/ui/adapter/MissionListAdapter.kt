package com.example.gloomhavestoryline2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gloomhavestoryline2.databinding.MissionViewBinding
import com.example.gloomhavestoryline2.db.entities.Mission

class MissionListAdapter(private var missions: List<Mission?>, private val onClick: (Mission) -> Unit): RecyclerView.Adapter<MissionListAdapter.MissionViewHolder>() {
    class MissionViewHolder private constructor(val binding: MissionViewBinding): RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): MissionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MissionViewBinding.inflate(layoutInflater, parent, false)
                return MissionViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder {
        return MissionViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return missions.size
    }

    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {
        holder.binding.also {
            it.mission = missions[position]
            it.root.setOnClickListener {
                onClick(missions[position]!!)
            }
        }
    }

    fun updateList(newList: List<Mission?>){
        missions = newList
        notifyDataSetChanged()
    }
}