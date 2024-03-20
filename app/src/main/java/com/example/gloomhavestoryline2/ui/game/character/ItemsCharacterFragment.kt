package com.example.gloomhavestoryline2.ui.game.character

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.DialogItemBinding
import com.example.gloomhavestoryline2.databinding.FragmentItemsCharacterBinding
import com.example.gloomhavestoryline2.db.entities.Item
import com.example.gloomhavestoryline2.db.repository.StorageRepository
import com.example.gloomhavestoryline2.ui.adapter.ItemCharacterListAdapter
import com.example.gloomhavestoryline2.view_model.GameViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ItemsCharacterFragment : Fragment() {

    private val TAG = "ITEMS_CHARACTER_FRAGMENT"
    private lateinit var binding: FragmentItemsCharacterBinding
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_items_character, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.findViewById<MaterialToolbar>(R.id.gameToolbar)?.subtitle = "Items"

        val recyclerView = binding.itemsRecyclerView
        val adapter =
            ItemCharacterListAdapter(emptyList(), requireContext()) { item -> onItemClick(item) }
        recyclerView.adapter = adapter

        gameViewModel.characterMain.observe(viewLifecycleOwner) { it ->
            adapter.updateAdapter(it.items.sortedBy { it.number })
            if (it.items.isNotEmpty()){
                binding.textViewNoItems.visibility = View.GONE
                binding.itemsRecyclerView.visibility = View.VISIBLE
            }
            else {
                binding.textViewNoItems.visibility = View.VISIBLE
                binding.itemsRecyclerView.visibility = View.GONE
            }

        }
    }

    private fun onItemClick(item: Item) {
        val view = DialogItemBinding.inflate(LayoutInflater.from(context))
        view.item = item
        view.gameViewModel = gameViewModel

        Glide.with(requireContext())
            .load(StorageRepository.downloadItemImage(item.image))
            .error(R.drawable.default_item)
            .into(view.imageView)

        context?.let {
            MaterialAlertDialogBuilder(it)
                .setView(view.root)
                .setPositiveButton(item.getHalfPrice()) {dialog,_ ->
                    gameViewModel.sellItem(item)
                    dialog.dismiss()
                }
                .show()
        }
    }

}