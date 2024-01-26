package com.example.gloomhavestoryline2.other

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.navOptions
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.db.repository.AuthRepository
import com.example.gloomhavestoryline2.db.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth

fun View.applySystemGestureInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemInsets = insets.systemGestureInsets

        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams

        layoutParams.leftMargin = systemInsets.left
        layoutParams.rightMargin = systemInsets.right
        layoutParams.bottomMargin = systemInsets.bottom

        WindowInsetsCompat.CONSUMED
    }
}

val navAnimations = navOptions {
    anim {
        enter = R.anim.slide_in_right
        exit = R.anim.slide_out_left
        popEnter = R.anim.slide_in_left
        popExit = R.anim.slide_out_right
    }
}

fun resetPassword(email: String) {
    AuthRepository().resetPassword(email)
}

