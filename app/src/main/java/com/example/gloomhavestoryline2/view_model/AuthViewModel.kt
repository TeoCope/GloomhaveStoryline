package com.example.gloomhavestoryline2.view_model

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.db.repository.AuthRepository
import com.example.gloomhavestoryline2.other.enum_class.RequestStatus
import com.example.gloomhavestoryline2.other.listeners.ToastMessage
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    private val TAG = "AUTH_VIEWMODEL"
    var toastMessage: ToastMessage? = null

    private val _status: MutableLiveData<RequestStatus> = MutableLiveData()
    val status: LiveData<RequestStatus>
        get() = _status

    private val _nickname_error: MutableLiveData<Int?> = MutableLiveData()
    val nickname_error: LiveData<Int?>
        get() = _nickname_error

    private val _email_error: MutableLiveData<Int?> = MutableLiveData()
    val email_error: LiveData<Int?>
        get() = _email_error

    private val _password_error: MutableLiveData<Int?> = MutableLiveData()
    val password_error: LiveData<Int?>
        get() = _password_error

    fun resetError(error: Int?){
        _nickname_error.value = error
        _email_error.value = error
        _password_error.value = error
    }

    fun login(email: String, password: String) {
        _status.value = RequestStatus.LOADING
        if (!emailValidation(email) or !passwordValidation(password)) {
            _status.value = RequestStatus.ERROR
            return
        }
        viewModelScope.launch {
            val result = AuthRepository().login(email,password)
            if (!result) {
                toastMessage?.showToast("Something went wrong!")
                _status.value = RequestStatus.ERROR
            } else {
                _status.value = RequestStatus.DONE
            }
        }
    }

    fun singup(nickname: String, email: String, password: String) {
        _status.value = RequestStatus.LOADING
        if (!nicknameValidation(nickname) or !emailValidation(email) or !passwordValidation(password)) {
            _status.value = RequestStatus.ERROR
            return
        }
        viewModelScope.launch {
            val result = AuthRepository().singup(nickname,email,password)
            if (!result) {
                toastMessage?.showToast("Something went wrong!")
                _status.value = RequestStatus.ERROR
                return@launch
            } else {
                _status.value = RequestStatus.DONE
            }
        }
    }

    fun resetPassword(email: String): Boolean {
        if (!emailValidation(email)) {
            return false
        }
        AuthRepository().resetPassword(email)
        return true
    }

    private fun nicknameValidation(nickname: String): Boolean {
        var check = false
        when {
            nickname.isEmpty() -> {
                _nickname_error.value = R.string.empty_field
            }
            else -> {
                check = true
                _nickname_error.value = null
            }
        }
        return check
    }

    private fun emailValidation(email: String): Boolean{
        var check = false
        when {
            email.isEmpty() -> {
                _email_error.value = R.string.empty_field
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _email_error.value = R.string.error_format_invalid
            }
            else -> {
                check= true
                _email_error.value = null
            }
        }
        Log.d(TAG, "Errore: $check")
        return check
    }

    private fun passwordValidation(password: String): Boolean{
        var chek = false
        when {
            password.isEmpty() -> {
                _password_error.value = R.string.empty_field
            }
            password.length < 8 -> {
                _password_error.value = R.string.error_too_short
            }
            else -> {
                chek = true
                _password_error.value = null
            }
        }
        return chek
    }
}