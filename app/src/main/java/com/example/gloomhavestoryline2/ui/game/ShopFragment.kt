package com.example.gloomhavestoryline2.ui.game

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentShopBinding
import com.example.gloomhavestoryline2.db.entities.Item
import com.example.gloomhavestoryline2.ui.adapter.ItemListAdapter
import com.example.gloomhavestoryline2.view_model.GameViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ShopFragment : Fragment() {

    private val TAG = "SHOP_FRAGMENT"
    private lateinit var binding: FragmentShopBinding
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shop, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.shopRecyclerView
        val adapter = ItemListAdapter(emptyList(),requireContext()) {item: Item -> onClick(item) }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        gameViewModel.items.observe(viewLifecycleOwner) {newItemList: List<Item> ->
            adapter.updateList(newItemList.filter { it.avail > 0 }.sortedBy { it.number })
        }
    }

    private fun onClick(item: Item){
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(item.getFullName())
                .setMessage(item.effect)
                .setPositiveButton("Buy: ${item.getPrice()}") {dialog, which ->
                    gameViewModel.buyItem(item)
                }.show()
        }
    }
}