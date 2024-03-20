package com.example.gloomhavestoryline2.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentHomeBinding
import com.example.gloomhavestoryline2.db.entities.Game
import com.example.gloomhavestoryline2.other.enum_class.RequestStatus
import com.example.gloomhavestoryline2.ui.adapter.GameListAdapter
import com.example.gloomhavestoryline2.ui.game.GameActivity
import com.example.gloomhavestoryline2.view_model.HomeViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

const val GAME_ID = "game id"

class HomeFragment : Fragment() {

    private val TAG = "HOME_FRAGMENT"

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var dialogView: View
    private lateinit var dialog: AlertDialog

    private lateinit var editText: TextInputLayout
    private lateinit var dropdownMenu: TextInputLayout

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
        editText = dialogView.findViewById(R.id.dialogNewGameTextField)
        dropdownMenu = dialogView.findViewById(R.id.dialogNewGameDropdownMenu)
        val btnNewGame = dialogView.findViewById<MaterialButton>(R.id.dialogBtnCreateNewGame)

        btnNewGame.setOnClickListener {
            val squadName = editText.editText?.text.toString().trim()
            val character = dropdownMenu.editText?.text.toString().trim()
            if (validateSquadName(squadName) or validateCharacter(character)) {
                //binding.fabNewGame.isEnabled = false
                dialog.dismiss()
                homeViewModel.newGame(squadName, character)
            }
        }

        val statusObserver = Observer<RequestStatus> { newStatus ->
            when(newStatus) {
                RequestStatus.LOADING -> {
                    binding.fabNewGame.isEnabled = false
                }
                else -> {
                    binding.fabNewGame.isEnabled = true
                    if (newStatus == RequestStatus.DONE) {
                        val intent = Intent(activity, GameActivity::class.java)
                        intent.putExtra(GAME_ID,homeViewModel.newGameId.value)
                        startActivity(intent)
                        activity?.finish()
                    }
                }
            }
        }
        homeViewModel.status.observe(this,statusObserver)

//        val newGameIdObserver = Observer<String> {newGameId ->
//            val intent = Intent(activity, GameActivity::class.java)
//            intent.putExtra(GAME_ID,newGameId)
//            startActivity(intent)
//            activity?.finish()
//        }
//        homeViewModel.newGameId.observe(viewLifecycleOwner, newGameIdObserver)
    }

    fun newGame() {
        context?.let {
            dialog = MaterialAlertDialogBuilder(it)
                .setView(dialogView)
                .show()
            Log.d(TAG, dialog.javaClass.name)
        }


    }

    private fun onGameClick(gameID: String){
        val intent = Intent(context, GameActivity::class.java)
        intent.putExtra(GAME_ID, gameID)
        startActivity(intent)
        activity?.finish()
    }

    private fun validateSquadName(squadName: String): Boolean{
        return when {
            squadName.isEmpty() -> {
                editText.error = getString(R.string.empty_field)
                false
            }
            squadName.length < 3 -> {
                editText.error = getString(R.string.error_too_short)
                false
            }
            else -> {
                editText.error = null
                true
            }
        }
    }

    private fun validateCharacter(character: String): Boolean{
        return when {
            character.isEmpty() -> {
                dropdownMenu.error = getString(R.string.empty_field)
                false
            }
            else -> {
                dropdownMenu.error = null
                true
            }
        }
    }
}