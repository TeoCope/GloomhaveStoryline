package com.example.gloomhavestoryline2.ui.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gloomhavestoryline2.MainActivity
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.ActivityGameBinding
import com.example.gloomhavestoryline2.view_model.GameViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)

        val gameId = intent.getStringExtra("gameId")

        val gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]
        if (gameId != null) {
            gameViewModel.setGame(gameId)
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val toolbar = binding.gameToolbar
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_game) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigation : BottomNavigationView = binding.bottomNavigationView
        bottomNavigation.setupWithNavController(navController)
    }
}