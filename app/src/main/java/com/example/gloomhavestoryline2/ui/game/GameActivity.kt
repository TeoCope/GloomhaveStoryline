package com.example.gloomhavestoryline2.ui.game

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gloomhavestoryline2.MainActivity
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.ActivityGameBinding
import com.example.gloomhavestoryline2.other.listeners.ProgressIndicator
import com.example.gloomhavestoryline2.other.listeners.ToastMessage
import com.example.gloomhavestoryline2.ui.home.GAME_ID
import com.example.gloomhavestoryline2.view_model.GameViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.progressindicator.LinearProgressIndicator

class GameActivity : AppCompatActivity(), ToastMessage, ProgressIndicator {

    private val TAG = "GAME_ACTIVITY"

    private lateinit var binding: ActivityGameBinding
    private lateinit var linearProgressIndicator: LinearProgressIndicator

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)

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

        val gameId = intent.getStringExtra(GAME_ID)
        Log.d(TAG, "$gameId")
        val gameViewModel: GameViewModel = ViewModelProvider(this)[GameViewModel::class.java]
        if (gameId != null) {
            gameViewModel.setGame(gameId)
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        gameViewModel.progressIndicator = this

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_game) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.storyline, R.id.shop, R.id.characther, R.id.party))

        val toolbar = binding.gameToolbar
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
            toolbar.subtitle = null
        }

        val bottomNavigation: BottomNavigationView = binding.bottomNavigationView
        bottomNavigation.setupWithNavController(navController)

        gameViewModel.toastMessage = this

        gameViewModel.squad.observe(this) {
            gameViewModel.setCharacterMain()
            binding.mainUser = gameViewModel.characterMain.value
        }

        gameViewModel.game.observe(this) {
            if (it.isEnd) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_top_app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                true
            }

            else -> false
        }
    }

    override fun setVisible() {
        binding.linearProgressIndicator.visibility = View.VISIBLE
    }

    override fun setGone() {
        binding.linearProgressIndicator.visibility = View.GONE
    }
}