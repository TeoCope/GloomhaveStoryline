package com.example.gloomhavestoryline2.ui.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.MissionViewBinding
import com.example.gloomhavestoryline2.db.entities.Mission

class MissionListAdapter(
    private var missions: List<Mission?>,
    private val onClick: (Mission) -> Unit
) : RecyclerView.Adapter<MissionListAdapter.MissionViewHolder>() {

    private val TAG = "MISSION_LIST_ADAPTER"

    class MissionViewHolder private constructor(val binding: MissionViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
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
        holder.binding.mission = missions[position]
        holder.binding.missionWrapper.setOnClickListener {
            onClick(missions[position]!!)
        }
        if (missions[position]?.isCompleted!!) {
            holder.binding.missionName.setTextColor(Color.rgb(0, 128, 0))
        }

    }

    fun updateList(newList: List<Mission?>) {
        missions = newList
        notifyDataSetChanged()
    }
}