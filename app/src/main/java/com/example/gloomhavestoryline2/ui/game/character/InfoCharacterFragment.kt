package com.example.gloomhavestoryline2.ui.game.character

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentInfoCharacterBinding
import com.example.gloomhavestoryline2.view_model.GameViewModel
import com.google.android.material.appbar.MaterialToolbar

class InfoCharacterFragment : Fragment() {

    private val TAG ="INFO_CHARACTER_FRAGMENT"
    private lateinit var binding: FragmentInfoCharacterBinding
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_info_character, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.findViewById<MaterialToolbar>(R.id.gameToolbar)?.subtitle = "Info"

        gameViewModel.characterMain.observe(viewLifecycleOwner) {
            binding.character = it
        }

        val moneyInputLayout = binding.moneyTextInputLayout
        val experienceInputLayout = binding.experienceTextInputLayout

        val btnAssign = binding.btnAssign
        btnAssign.setOnClickListener {
            val money = moneyInputLayout.editText?.text.toString().trim()
            val experience = experienceInputLayout.editText?.text.toString().trim()
            var check = true
            if (money.isEmpty()) {
                check = false
                moneyInputLayout.error = getString(R.string.empty_field)
            } else {
                moneyInputLayout.error = null
            }
            if (experience.isEmpty()) {
                check = false
                experienceInputLayout.error = getString(R.string.empty_field)
            } else {
                experienceInputLayout.error = null
            }
            if (check) {
                gameViewModel.assignUser(money,experience)
                moneyInputLayout.editText?.text?.clear()
                experienceInputLayout.editText?.text?.clear()
            }
        }
    }

    override fun onPause() {
        super.onPause()
            activity?.supportFragmentManager?.popBackStack(R.layout.fragment_info_character,FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}