package com.example.gloomhavestoryline2.view_model

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.db.repository.AuthRepository
import com.example.gloomhavestoryline2.db.repository.FirebaseRepository
import com.example.gloomhavestoryline2.other.enum_class.RequestStatus
import com.example.gloomhavestoryline2.other.listeners.ProgressIndicatorListener
import com.example.gloomhavestoryline2.other.`object`.EditTextError
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    private val TAG = "AUTH_VIEWMODEL"
    var progressIndicatorListener: ProgressIndicatorListener? = null

    private val _status: MutableLiveData<RequestStatus> = MutableLiveData()
    val status: LiveData<RequestStatus>
        get() = _status

    private val _email_error: MutableLiveData<Int> = MutableLiveData()
    val email_error: LiveData<Int>
        get() = _email_error

    private val _password_error: MutableLiveData<Int> = MutableLiveData()
    val password_error: LiveData<Int>
        get() = _password_error

    fun login(email: String, password: String) {
        progressIndicatorListener?.isVisible()
        _status.value = RequestStatus.LOADING
        if (!inputValidation(email, password)) {
            progressIndicatorListener?.isGone()
            _status.value = RequestStatus.ERROR
            return
        }
        viewModelScope.launch {
            val result = AuthRepository().login(email,password)
            result?.let {
                progressIndicatorListener?.isGone()
                _status.value = RequestStatus.DONE
            }
        }
    }

    private fun inputValidation(email: String, password: String): Boolean{
        var check: Boolean = false
        when {
            email.isEmpty() -> {
                _email_error.value = R.string.empty_field
            }
            Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _email_error.value = R.string.error_format_invalid
            }
            password.isEmpty() -> {
                _password_error.value = R.string.empty_field
            }
            password.length < 8 -> {
                _password_error.value = R.string.error_too_short
            }
            else -> check= true
        }
        return check
    }
}