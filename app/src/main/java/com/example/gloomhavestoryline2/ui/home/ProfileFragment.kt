package com.example.gloomhavestoryline2.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentProfileBinding
import com.example.gloomhavestoryline2.db.entities.User
import com.example.gloomhavestoryline2.other.enum_class.RequestStatus
import com.example.gloomhavestoryline2.other.resetPassword
import com.example.gloomhavestoryline2.ui.auth.AuthActivity
import com.example.gloomhavestoryline2.view_model.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileFragment = this
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        val userObserver = Observer<User> {newUser ->
            binding.user = newUser
        }
        homeViewModel.userLogged.observe(viewLifecycleOwner, userObserver)

        val statusObserver = Observer<RequestStatus> {newRequest ->
            when {
                newRequest == RequestStatus.DONE -> {
                    startActivity(Intent(context, AuthActivity::class.java))
                    activity?.finish()
                }
                newRequest == RequestStatus.ERROR -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
        homeViewModel.status.observe(viewLifecycleOwner, statusObserver)
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(activity, AuthActivity::class.java))
        activity?.finish()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_top_app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        return when(item.itemId) {
            R.id.resetPassword -> {
                resetPassword(user?.email!!)
                Toast.makeText(context,"Check your email address", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.deleteAccount -> {
                buildAllertDialog()
                true
            }
            else -> false
        }
    }

    private fun buildAllertDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want delete this account?")
                .setPositiveButton("Confim") {dialog, which ->
                    homeViewModel.deleteAccount()
                }
                .setNegativeButton("No") {_, _ ->}
                .show()
        }
    }

}