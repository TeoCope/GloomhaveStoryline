package com.example.gloomhavestoryline2.other.`object`

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object EditTextError {
    private val _error: MutableLiveData<String?> = MutableLiveData()
    val error: LiveData<String?>
        get() = _error

    fun validateText(string: String): Boolean{
        return when {
             empyString(string) -> {
                _error.value = "Empty Field"
                false
            }
            else -> {
                _error.value = null
                true
            }
        }
    }

    fun validateEmail(string: String): Boolean{
        return when {
            empyString(string) -> {
                _error.value = "Empty Field"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(string).matches() -> {
                _error.value = "Wrong Email Pattern"
                false
            }
            else -> {
                _error.value = null
                true
            }
        }
    }

    fun validatePassword(string: String): Boolean{
        return when{
            empyString(string) -> {
                _error.value =
            }
        }
    }

    private fun empyString(string: String): Boolean{
        return if (string.isEmpty())
            false
        else
            true
    }
}