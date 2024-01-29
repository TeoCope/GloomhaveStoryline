package com.example.gloomhavestoryline2.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentCharactherBinding
import com.example.gloomhavestoryline2.other.navAnimations
import com.example.gloomhavestoryline2.view_model.GameViewModel

class CharactherFragment : Fragment() {

    private val TAG = "CHARACTER_FRAGMENT"

    private lateinit var binding: FragmentCharactherBinding
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_characther, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnInfo = binding.btnInfo
        btnInfo.setOnClickListener {
            findNavController().navigate(R.id.infoCharacter,null, navAnimations)
        }
        val btnAbilities = binding.btnAbilities
        btnAbilities.setOnClickListener {
            findNavController().navigate(R.id.abilitiesCharacter,null, navAnimations)
        }
        val btnItems = binding.btnItems
        btnItems.setOnClickListener {
            findNavController().navigate(R.id.itemsCharacter, null, navAnimations)
        }
    }

}