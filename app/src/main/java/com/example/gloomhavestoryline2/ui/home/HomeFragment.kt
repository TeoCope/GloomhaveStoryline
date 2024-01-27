package com.example.gloomhavestoryline2.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentHomeBinding
import com.example.gloomhavestoryline2.db.entities.Game
import com.example.gloomhavestoryline2.db.entities.User
import com.example.gloomhavestoryline2.other.enum_class.RequestStatus
import com.example.gloomhavestoryline2.other.navAnimations
import com.example.gloomhavestoryline2.ui.adapter.GameListAdapter
import com.example.gloomhavestoryline2.ui.game.GameActivity
import com.example.gloomhavestoryline2.view_model.HomeViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputLayout

const val GAME_ID = "game id"

class HomeFragment : Fragment() {

    private val TAG = "HOME_FRAGMENT"

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var dialogView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeFragment = this

        val recyclerView = binding.homeRecyclerView
        val adapter = GameListAdapter(emptyList()) { gameID -> onGameClick(gameID) }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val gameListObserver = Observer<List<Game>> {newGameList ->
            if (newGameList.isNotEmpty()) {
                binding.textViewNoGames.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
                adapter.updateGameList(newGameList)
            } else {
                binding.homeRecyclerView.visibility = View.GONE
                binding.textViewNoGames.visibility = View.VISIBLE
            }
        }
        homeViewModel.gameList.observe(viewLifecycleOwner,gameListObserver)

        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_newgame, null)
        val editText = dialogView.findViewById<TextInputLayout>(R.id.dialogNewGameTextField)
        val dropdownMenu = dialogView.findViewById<TextInputLayout>(R.id.dialogNewGameDropdownMenu)
        val btnNewGame = dialogView.findViewById<MaterialButton>(R.id.dialogBtnCreateNewGame)

        btnNewGame.setOnClickListener {
            val squadName = editText.editText?.text.toString().trim()
            val character = dropdownMenu.editText?.text.toString().trim()
            homeViewModel.newGame(squadName, character)
        }

        val squadNameErrorObserver = Observer<Int?> {newError ->
            if (newError != null)
                editText.error = getString(newError)
            else
                editText.error = newError
        }
        homeViewModel.squadNameError.observe(viewLifecycleOwner,squadNameErrorObserver)

        val characterErrorObserver = Observer<Int?> {newError ->
            if (newError != null) {
                dropdownMenu.error = getString(newError)
            } else {
                dropdownMenu.error = null
            }
        }
        homeViewModel.characterError.observe(viewLifecycleOwner,characterErrorObserver)

        val newGameIdObserver = Observer<String> {newGameId ->
            val intent = Intent(activity, GameActivity::class.java)
            intent.putExtra(GAME_ID,newGameId)
            startActivity(intent)
            activity?.finish()
        }
        homeViewModel.newGameId.observe(viewLifecycleOwner, newGameIdObserver)
    }

    fun newGame() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setView(dialogView)
                .show()
        }
    }

    private fun onGameClick(gameID: String){
        val intent = Intent(context, GameActivity::class.java)
        intent.putExtra(GAME_ID, gameID)
        startActivity(intent)
        activity?.finish()
    }
}