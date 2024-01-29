package com.example.gloomhavestoryline2.ui.game.character

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentItemsCharacterBinding
import com.example.gloomhavestoryline2.db.entities.Item
import com.example.gloomhavestoryline2.ui.adapter.ItemCharacterListAdapter
import com.example.gloomhavestoryline2.view_model.GameViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ItemsCharacterFragment : Fragment() {

    private val TAG = "ITEMS_CHARACTER_FRAGMENT"
    private lateinit var binding: FragmentItemsCharacterBinding
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_items_character, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.itemsRecyclerView
        val adapter = ItemCharacterListAdapter(emptyList()) {item -> onItemClick(item)}
        recyclerView.adapter = adapter

        gameViewModel.characterMain.observe(viewLifecycleOwner) { it ->
            adapter.updateAdapter(it.items.sortedBy { it.number })
        }
    }

    private fun onItemClick(item: Item) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(item.getFullName())
                .setMessage(item.effect)
                .setPositiveButton("Sell: ${item.getHalfPrice()}") {dialog, _ ->
                    gameViewModel.sellItem(item)
                    dialog.dismiss()
                }.show()
        }
    }

}