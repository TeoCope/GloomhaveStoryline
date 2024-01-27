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
        gameViewModel.game.observe(viewLifecycleOwner) {newGame: Game ->
            adapter.updateList(newGame.missions)
        }

    }

    fun onMissionClick(mission: Mission) {
        context?.let {
           val dialog = MaterialAlertDialogBuilder(it)
                .setTitle(mission.name)
                .setMessage(getString(R.string.dialog_mission_complete))
                .setPositiveButton("Completed") {_,_ ->}
                    .show()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                Log.d(TAG,"${gameViewModel.missionCompleted()} ${gameViewModel.game.value?.currentMission}")
            }
        }
    }
}