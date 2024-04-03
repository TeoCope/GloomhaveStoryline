package com.example.gloomhavestoryline2.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentJoinGameBinding
import com.example.gloomhavestoryline2.db.entities.Game
import com.example.gloomhavestoryline2.other.listeners.ProgressIndicator
import com.example.gloomhavestoryline2.ui.game.GameActivity
import com.example.gloomhavestoryline2.view_model.HomeViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout


class JoinGameFragment : Fragment(), ProgressIndicator {

    private val TAG = "JOIN_GAME_FRAGMENT"
    private lateinit var binding: FragmentJoinGameBinding
    private val homeViewModel: HomeViewModel by activityViewModels()

    private val linearProgressIndicator: LinearProgressIndicator? = activity?.findViewById(R.id.linearProgressIndicator)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_join_game,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeViewModel = homeViewModel
        binding.joinGameFragment = this

        homeViewModel.progressIndicator = this

        val searchTextView: TextInputLayout = binding.gameCodeTextInputLayout
        val gameID = arguments?.getString("gameID")
        if (gameID != null) {
            searchTextView.editText?.setText(gameID)
        }

        val gameInfoWrapper = binding.gameInfoWrapper
        val dropdownMenu = binding.dropdownMenu
        homeViewModel.gameResult.observe(viewLifecycleOwner) {newGame: Game? ->
            if (newGame != null) {
                val characterAvailable = newGame.charactersAvailable
                (dropdownMenu.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(
                    characterAvailable.toTypedArray()
                )
                binding.game = newGame
                gameInfoWrapper.visibility = View.VISIBLE
            }
            else
                gameInfoWrapper.visibility = View.GONE
        }

        homeViewModel.searchGameError.observe(viewLifecycleOwner) {newError: Int? ->
            if (newError != null)
                searchTextView.error = getString(newError)
            else
                searchTextView.error = newError
        }
    }

    override fun onPause() {
        super.onPause()
        homeViewModel.lostFocus()
    }

    fun onSearchClick() {
        val gameId = binding.gameCodeTextInputLayout.editText?.text.toString().trim()
        homeViewModel.searchGame(gameId)
    }

    fun onJoinGameClick() {
        val character = binding.dropdownMenu.editText?.text.toString()
        homeViewModel.joinGame(character)
    }

    override fun setVisible() {
        linearProgressIndicator?.visibility = View.VISIBLE
    }

    override fun setGone() {
        linearProgressIndicator?.visibility = View.GONE
        val gameID = homeViewModel.gameResult.value?.id
        val intent = Intent(context, GameActivity::class.java)
        intent.putExtra(GAME_ID, gameID)
        startActivity(intent)
    }

}