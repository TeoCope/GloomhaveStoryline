package com.example.gloomhavestoryline2.other

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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