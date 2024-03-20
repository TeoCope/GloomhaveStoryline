package com.example.gloomhavestoryline2.ui.game.character

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentAbilitiesCharacterBinding
import com.example.gloomhavestoryline2.db.entities.Ability
import com.example.gloomhavestoryline2.ui.adapter.AbilitiesListAdapter
import com.example.gloomhavestoryline2.view_model.GameViewModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AbilitiesCharacterFragment : Fragment() {

    private val TAG ="ABILITIES_CHARACTER_FRAGMENT"
    private lateinit var binding: FragmentAbilitiesCharacterBinding
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_abilities_character, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.findViewById<MaterialToolbar>(R.id.gameToolbar)?.subtitle = "Abilities"

        val recyclerView = binding.abilitiesRecyclerView
        val adapter = AbilitiesListAdapter(emptyList(),requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = FlexboxLayoutManager(context).apply {
            justifyContent = JustifyContent.SPACE_AROUND
            alignItems = AlignItems.CENTER
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }

        val userLevel = gameViewModel.characterMain.value?.level
        gameViewModel.characterMain.observe(viewLifecycleOwner) { it ->
            adapter.updateAdapter(it.abilities.filter { it.level <= userLevel!! }.sortedBy { it.level })
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.findViewById<MaterialToolbar>(R.id.gameToolbar)?.subtitle = null
    }
}