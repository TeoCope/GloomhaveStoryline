package com.example.gloomhavestoryline2.ui.game

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentPartyBinding
import com.example.gloomhavestoryline2.db.entities.Character
import com.example.gloomhavestoryline2.ui.adapter.SquadListAdapter
import com.example.gloomhavestoryline2.view_model.GameViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PartyFragment : Fragment() {

    private val TAG = "PARTY_FRAGMENT"
    private lateinit var binding: FragmentPartyBinding
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_party, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.partyRecyclerView
        val adapter = SquadListAdapter(emptyList(),requireContext())
        recyclerView.adapter = adapter

        val userID = Firebase.auth.uid
        gameViewModel.squad.observe(viewLifecycleOwner) {newSquad: List<Character> ->
            adapter.updateList(newSquad.filter { it.id != userID })
        }

        binding.game = gameViewModel.game.value

        val btnInviteFriend = binding.btnInviteFriend
        btnInviteFriend.setOnClickListener {
            inviteFriend()
        }

        val btnDeleteGame = binding.btnDeleteGame
        btnDeleteGame.setOnClickListener {
            deleteGame()
            it.isEnabled = false
        }
    }

    private fun inviteFriend() {
        val gameID = gameViewModel.game.value?.id
        val uriString = "www.gloomhaven.com/$gameID"
        val fullMessage = "Join my game using this link:\n $uriString"

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, fullMessage)
        intent.type = "text/plain"

        val chooser = Intent.createChooser(intent, "Share invite with friends")

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(chooser)
        } else {
            Toast.makeText(context, "No apps available for sharing invite code", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteGame() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Delete Game")
                .setMessage("Are you sure you want to delete the game?")
                .setPositiveButton("Confirm") {dialog,_ ->
                    gameViewModel.deleteGame()
                    dialog.dismiss()
                }.show()
        }
    }
}