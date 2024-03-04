package com.example.gloomhavestoryline2.ui.auth

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_auth)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())) {
                windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())

            } else {
                windowInsetsController.show(WindowInsetsCompat.Type.navigationBars())
            }
            view.onApplyWindowInsets(windowInsets)
        }

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