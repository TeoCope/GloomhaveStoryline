package com.example.gloomhavestoryline2.ui.home

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentProfileBinding
import com.example.gloomhavestoryline2.db.entities.User
import com.example.gloomhavestoryline2.other.enum_class.RequestStatus
import com.example.gloomhavestoryline2.other.resetPassword
import com.example.gloomhavestoryline2.ui.auth.AuthActivity
import com.example.gloomhavestoryline2.view_model.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileFragment : Fragment() {

    private val TAG = "PROFILE_FRAGMENT"
    private val PERMISSION_REQUEST_STORAGE = 0

    private lateinit var binding: FragmentProfileBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var imageUri: Uri
    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileFragment = this
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        val userImageView = binding.imageViewUser

        val uriObserver = Observer<Uri> { newUri ->
            Glide.with(requireContext())
                .load(newUri)
                .circleCrop()
                .error(R.drawable.default_user)
                .into(userImageView)
        }
        homeViewModel.uri.observe(viewLifecycleOwner, uriObserver)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    launchNewPhotoPicker()
                } else {
                    Toast.makeText(context, "Please grant permission", Toast.LENGTH_LONG).show()

                }
            }

        userImageView.setOnClickListener {
            val permission =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
            when {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    permission
                ) -> {
                    Snackbar.make(requireView(), getString(R.string.permission_required), Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok") {
                            requestPermissionLauncher.launch(permission)
                        }
                        .show()
                }
                else -> {
                    requestPermissionLauncher.launch(permission)
                }
            }
        }

        val userObserver = Observer<User> { newUser ->
            binding.user = newUser
        }
        homeViewModel.userLogged.observe(viewLifecycleOwner, userObserver)

        val statusObserver = Observer<RequestStatus> { newRequest ->
            when (newRequest) {
                RequestStatus.LOADING -> {
                    userImageView.isEnabled = false
                }

                RequestStatus.DONE -> {
                    if (homeViewModel.requestCode.value == 1) {
                        userImageView.isEnabled = true
                    } else if (homeViewModel.requestCode.value == 2) {
                        startActivity(Intent(context, AuthActivity::class.java))
                        activity?.finish()
                    }
                }

                RequestStatus.ERROR -> {
                    if (homeViewModel.requestCode.value == 1) {
                        userImageView.isEnabled = true
                    } else if (homeViewModel.requestCode.value == 2) {
                        Snackbar.make(layout,getString(R.string.error),Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
        homeViewModel.status.observe(viewLifecycleOwner, statusObserver)
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(activity, AuthActivity::class.java))
        activity?.finish()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_top_app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        return when (item.itemId) {
            R.id.resetPassword -> {
                resetPassword(user?.email!!)
                Snackbar.make(layout, "Check your email address", Snackbar.LENGTH_SHORT).show()
                true
            }

            R.id.deleteAccount -> {
                buildAlertDialog()
                true
            }

            else -> false
        }
    }

    private fun buildAlertDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want delete this account?")
                .setPositiveButton("Confim") { _, _ ->
                    homeViewModel.deleteAccount()
                }
                .setNegativeButton("No") { _, _ -> }
                .show()
        }
    }

    private fun launchNewPhotoPicker() {
        newPhotoPiker.launch("image/*")
    }

    private val newPhotoPiker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                homeViewModel.setUserImage(it)
            }
        }
}