package com.example.gloomhavestoryline2.other

import androidx.navigation.navOptions
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.db.repository.AuthRepository

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

