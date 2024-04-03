package com.example.gloomhavestoryline2

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gloomhavestoryline2.db.entities.User
import com.example.gloomhavestoryline2.other.enum_class.RequestStatus
import com.example.gloomhavestoryline2.other.listeners.ToastMessage
import com.example.gloomhavestoryline2.ui.auth.AuthActivity
import com.example.gloomhavestoryline2.view_model.HomeViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), ToastMessage {

    private val TAG = "MAIN_ACTIVITY"


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        homeViewModel.toastMessage = this

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        val navController = navHostFragment.navController

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setupWithNavController(navController)

        val userObserver = Observer<User> { newUser ->
            homeViewModel.setGameList(newUser.games)
        }
        homeViewModel.userLogged.observe(this,userObserver)

        val statusObserver = Observer<RequestStatus> { newStatus ->
            when(newStatus) {
                RequestStatus.LOADING -> {
                    setVisible()
                }
                else -> {
                    setGone()
                }
            }
        }
        homeViewModel.status.observe(this,statusObserver)

    }

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startActivity(Intent(this,AuthActivity::class.java))
            finish()
        }
    }

    override fun showSnackbar(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun setVisible() {
        findViewById<LinearProgressIndicator>(R.id.linearProgressIndicator).visibility = View.VISIBLE
    }

    fun setGone() {
        findViewById<LinearProgressIndicator>(R.id.linearProgressIndicator).visibility = View.GONE
    }
}