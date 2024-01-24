package com.example.gloomhavestoryline2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginLeft
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import com.example.gloomhavestoryline2.view_model.FirestoreViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firestoreViewModel = ViewModelProvider(this)[FirestoreViewModel::class.java]
        val rootView: View = findViewById(android.R.id.content)
    }
}