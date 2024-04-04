package com.example.gloomhavestoryline2.view_model

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.db.repository.AuthRepository
import com.example.gloomhavestoryline2.other.TextInputError
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
        val emailValidation = TextInputError(email).emailValidation()
        val passwordValidation = TextInputError(password).passwordValidation()
        _email_error.value = emailValidation
        _password_error.value = passwordValidation
        if (emailValidation != null || passwordValidation != null) {
            _status.value = RequestStatus.ERROR
            return
        }
        viewModelScope.launch {
            val result = AuthRepository().login(email,password)
            if (!result) {
                toastMessage?.showSnackbar("Something went wrong!")
                _status.value = RequestStatus.ERROR
            } else {
                _status.value = RequestStatus.DONE
            }
        }
    }

    fun singUp(nickname: String, email: String, password: String) {
        _status.value = RequestStatus.LOADING
        val nicknameValidation = TextInputError(nickname).nicknameValidation()
        val emailValidation = TextInputError(email).emailValidation()
        val passwordValidation = TextInputError(password).passwordValidation()
        _nickname_error.value = nicknameValidation
        _email_error.value = emailValidation
        _password_error.value = passwordValidation
        if (nicknameValidation != null || emailValidation != null || passwordValidation != null) {
            _status.value = RequestStatus.ERROR
            return
        }
        viewModelScope.launch {
            val result = AuthRepository().singUp(nickname,email,password)
            if (!result) {
                toastMessage?.showSnackbar("Something went wrong!")
                _status.value = RequestStatus.ERROR
                return@launch
            } else {
                _status.value = RequestStatus.DONE
            }
        }
    }

    fun resetPassword(email: String): Boolean {
        val validation = TextInputError(email).emailValidation()
        _email_error.value = validation
        if (validation != null) {
            return false
        }
        AuthRepository().resetPassword(email)
        return true
    }
}