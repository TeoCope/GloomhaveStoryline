package com.example.gloomhavestoryline2.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.ActivityAuthBinding
import com.example.gloomhavestoryline2.view_model.AuthViewModel

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_auth)

        val toolbar = binding.topAppBar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.auth_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.singupFragment,R.id.loginFragment))
        toolbar.setupWithNavController(navController,appBarConfiguration)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        authViewModel.resetError(null)
    }
}