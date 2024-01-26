package com.example.gloomhavestoryline2.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.db.entities.Game
import com.example.gloomhavestoryline2.db.entities.User
import com.example.gloomhavestoryline2.db.repository.AuthRepository
import com.example.gloomhavestoryline2.db.repository.FirebaseRepository
import com.example.gloomhavestoryline2.db.repository.StorageRepository
import com.example.gloomhavestoryline2.other.enum_class.RequestStatus
import com.example.gloomhavestoryline2.other.listeners.ToastMessage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val TAG = "HOME_VIEW_MODEL"
    var toastMessage: ToastMessage? = null

    private val _status: MutableLiveData<RequestStatus> = MutableLiveData()
    val status: LiveData<RequestStatus>
        get() = _status

    private val _userLogged: MutableLiveData<User> = MutableLiveData()
    val userLogged: LiveData<User>
        get() = _userLogged

    private val _gameList: MutableLiveData<List<Game>> = MutableLiveData()
    val gameList: LiveData<List<Game>>
        get() = _gameList

    private val _newGameId: MutableLiveData<String> = MutableLiveData()
    val newGameId: LiveData<String>
        get() = _newGameId

    private val _squadNameError: MutableLiveData<Int?> = MutableLiveData()
    val squadNameError: LiveData<Int?>
        get() = _squadNameError

    private val _characterError: MutableLiveData<Int?> = MutableLiveData()
    val characterError: LiveData<Int?>
        get() = _characterError

    init {
        setUserLogged()
    }

    fun setUserLogged(){
        /*val userID = FirebaseAuth.getInstance().currentUser?.uid
        viewModelScope.launch {
            val result = userID?.let { FirebaseRepository.getUser(it) }
            if (result == null) {
                return@launch
            }
            _userLogged.value = result!!
        }*/
        val userID = Firebase.auth.currentUser?.uid
        val userDocumentReference = userID?.let { FirebaseRepository.getUser(it) }
        userDocumentReference?.addSnapshotListener{ user, error ->
            if (error != null) {
                Log.e(TAG, "User listen failed", error)
                return@addSnapshotListener
            }

            if (user != null && user.exists()){
                _userLogged.value = user.toObject(User::class.java)
            } else {
                Log.d(TAG, "Current user data: null")
            }
        }
    }

    fun setGameList(gamesId: List<String>) {
        viewModelScope.launch {
            _gameList.value = FirebaseRepository.getGames(gamesId)
        }
    }

    fun deleteAccount() {
        _status.value = RequestStatus.LOADING
        viewModelScope.launch {
            if (FirebaseRepository.deleteUser(userLogged.value?.id!!)){
                Log.d(TAG, "Inizio eliminazione utente: ${status.value}")
                try {
                    AuthRepository().deleteUser()
                    _status.value = RequestStatus.DONE
                    Log.d(TAG, "Utente eliminato: ${status.value}")
                } catch (e: Exception) {
                    toastMessage?.showToast("Something went wrong!")
                    Log.d(TAG, "Error to delete ${userLogged.value}")
                    _status.value = RequestStatus.ERROR
                    Log.d(TAG, "Errore eliminazione utente: ${status.value}")
                }
            }
        }
    }

    fun dowloadRulebook(){
        viewModelScope.launch {
            Log.d(TAG, "Inizio download rulebook")
            StorageRepository.downloadRule()
        }
    }

    fun newGame(squadName: String, character: String) {
        val squadNameisValid = validateSquadName(squadName)
        val characterIsValid = validateCharacter(character)
        if (!squadNameisValid || !characterIsValid) {
            return
        }
        viewModelScope.launch {
            val result = FirebaseRepository.newGame(squadName, character, userLogged.value!!)
            if (result == null) {
                toastMessage?.showToast("Something went wront!")
                return@launch
            }
            _newGameId.value = result!!
        }
    }

    private fun validateSquadName(squadName: String): Boolean{
        return when {
            squadName.isEmpty() -> {
                _squadNameError.value = R.string.empty_field
                false
            }
            squadName.length < 3 -> {
                _squadNameError.value = R.string.error_too_short
                false
            }
            else -> {
                _squadNameError.value = null
                true
            }
        }
    }

    private fun validateCharacter(character: String): Boolean{
        return when {
            character.isEmpty() -> {
                _characterError.value = R.string.empty_field
                false
            }
            else -> {
                _characterError.value = null
                true
            }
        }
    }
}