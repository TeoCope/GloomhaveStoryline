package com.example.gloomhavestoryline2.ui.home

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentHomeBinding
import com.example.gloomhavestoryline2.db.entities.Game
import com.example.gloomhavestoryline2.other.`object`.EditTextError
import com.example.gloomhavestoryline2.other.applySystemGestureInsets
import com.example.gloomhavestoryline2.other.listeners.ProgressIndicatorListener
import com.example.gloomhavestoryline2.ui.adapter.GameListAdapter
import com.example.gloomhavestoryline2.view_model.FirestoreViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), ProgressIndicatorListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val TAG = "HOME_FRAGMENT"

    private lateinit var binding: FragmentHomeBinding
    private val firestoreViewModel: FirestoreViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applySystemGestureInsets()

        binding.homeFragment = this

        val recyclerView = binding.homeRecyclerView
        val adapter = GameListAdapter(emptyList())
        firestoreViewModel.progressIndicatorListener = this

        firestoreViewModel.setGames("sGHx44FS6L1IFa51lEqM","3XTaQ8uKHuKBXWrRVDJ8")

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val gameObserver = Observer<List<Game>> {newGameList ->
            adapter.updateGameList(newGameList)
        }
        firestoreViewModel.games.observe(viewLifecycleOwner, gameObserver)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun startProcess() {
        activity?.findViewById<LinearProgressIndicator>(R.id.linearProgressIndicator)?.visibility = View.VISIBLE
    }

    override fun endProcess() {
        activity?.findViewById<LinearProgressIndicator>(R.id.linearProgressIndicator)?.visibility = View.GONE
    }

    fun newGame() {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_newgame,null)
        val editText = view.findViewById<TextInputLayout>(R.id.dialogNewGameTextField)
        context?.let {
           val dialog =  MaterialAlertDialogBuilder(it)
                .setView(view)
                .setNeutralButton("Bottone Neutrale") { dialog, which ->

            }.show()

            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(View.OnClickListener {
                var closeDialog = false
                val gameName = editText.editText?.text.toString().trim()
                EditTextError.error.observe(this) { error ->
                    editText.error = error
                }
                if (firestoreViewModel.newGame(gameName))
                    closeDialog = true
                if (closeDialog)
                    dialog.dismiss()
            })
        }
    }
}