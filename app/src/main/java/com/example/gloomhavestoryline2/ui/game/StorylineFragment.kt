package com.example.gloomhavestoryline2.ui.game

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentStorylineBinding
import com.example.gloomhavestoryline2.db.entities.Game
import com.example.gloomhavestoryline2.db.entities.Mission
import com.example.gloomhavestoryline2.ui.adapter.MissionListAdapter
import com.example.gloomhavestoryline2.view_model.GameViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class StorylineFragment : Fragment() {

    private val TAG = "STORYLINE_FRAGMENT"

    private lateinit var binding: FragmentStorylineBinding
    private val gameViewModel: GameViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_storyline, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.missionRecyclerView
        val adapter = MissionListAdapter(emptyList()) {mission -> onMissionClick(mission)}

        recyclerView.adapter = adapter

        Log.d(TAG, "${gameViewModel.game.value}")
        gameViewModel.missions.observe(viewLifecycleOwner) {newList: List<Mission> ->
            if (gameViewModel.game.value?.currentMission == newList.size){
                gameViewModel.gameCompleted()
                gameViewModel.deleteGame()
            }
            val listFiltered = newList.filter { it.number <= gameViewModel.game.value?.currentMission!! + 1 }.sortedBy { it.number }
            if (listFiltered.isEmpty()) {
                adapter.updateList(newList.filter { it.number == 1 })
            } else {
                adapter.updateList(listFiltered)
            }

        }

    }

    private fun onMissionClick(mission: Mission) {
        if (mission.isCompleted) {
            return
        }
        context?.let {
           MaterialAlertDialogBuilder(it)
                .setTitle(mission.name)
                .setMessage(getString(R.string.dialog_mission_complete))
                .setPositiveButton("Completed") {dialog,_ ->
                    gameViewModel.missionCompleted(mission.name)
                    dialog.dismiss()
                }
                    .show()
        }
    }
}