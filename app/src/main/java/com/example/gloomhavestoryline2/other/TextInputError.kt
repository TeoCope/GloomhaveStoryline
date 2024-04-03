package com.example.gloomhavestoryline2.other

import android.util.Log
import android.util.Patterns
import androidx.core.util.PatternsCompat
import com.example.gloomhavestoryline2.R

class TextInputError(val word: String) {

    fun nicknameValidation(): Int? {
        return when {
            word.isEmpty() -> {
                R.string.empty_field
            }
            else -> {
                null
            }
        }
    }

    fun emailValidation(): Int?{
        return when {
            word.isEmpty() -> {
                R.string.empty_field
            }
            !isValidEmail()-> {
                R.string.error_format_invalid
            }
            else -> {
                null
            }
        }
    }

    private fun isValidEmail(): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(word).matches()
    }

    fun passwordValidation(): Int?{
        return when {
            word.isEmpty() -> {
                R.string.empty_field
            }
            word.length < 8 -> {
                R.string.error_too_short
            }
            else -> {
                null
            }
        }
    }
}