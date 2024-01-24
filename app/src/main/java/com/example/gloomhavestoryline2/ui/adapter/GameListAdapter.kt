package com.example.gloomhavestoryline2.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gloomhavestoryline2.databinding.GameViewBinding
import com.example.gloomhavestoryline2.db.entities.Game

class GameListAdapter(private var gameList: List<Game>): RecyclerView.Adapter<GameListAdapter.GameViewHolder>() {

    class GameViewHolder private constructor(val binding: GameViewBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): GameViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GameViewBinding.inflate(layoutInflater, parent, false)
                return GameViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.binding.game = gameList[position]
    }

    fun updateGameList(newList: List<Game>) {
        gameList = newList
        notifyDataSetChanged()
        Log.d("Adapter", "Lista aggiornata: $gameList")
    }


}