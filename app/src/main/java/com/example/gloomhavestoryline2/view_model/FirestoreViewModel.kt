package com.example.gloomhavestoryline2.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gloomhavestoryline2.db.entities.Character
import com.example.gloomhavestoryline2.db.entities.Game
import com.example.gloomhavestoryline2.db.entities.Item
import com.example.gloomhavestoryline2.db.entities.Mission
import com.example.gloomhavestoryline2.db.entities.User
import com.example.gloomhavestoryline2.db.repository.FirebaseRepository
import com.example.gloomhavestoryline2.other.`object`.EditTextError
import com.example.gloomhavestoryline2.other.listeners.ProgressIndicatorListener
import kotlinx.coroutines.launch

class FirestoreViewModel: ViewModel() {

    val TAG = "FIRESTORE_VIEW_MODEL"

    var progressIndicatorListener: ProgressIndicatorListener? = null

    private val _error: MutableLiveData<String?> = MutableLiveData()
    val error: LiveData<String?>
        get() = _error

    private val _missions: MutableLiveData<List<Mission>> = MutableLiveData()
    val missions: LiveData<List<Mission>>
        get() = _missions
    private val  _items: MutableLiveData<List<Item>> = MutableLiveData()
    val items: LiveData<List<Item>>
        get() = _items
    private val _characters: MutableLiveData<List<Character>> = MutableLiveData()
    val characters: LiveData<List<Character>>
        get() = _characters
    private val _users: MutableLiveData<List<User>> = MutableLiveData()
    val users: LiveData<List<User>>
        get() = _users
    private val _games: MutableLiveData<List<Game>> = MutableLiveData()
    val games: LiveData<List<Game>>
        get() = _games

    init {
        viewModelScope.launch {
            _missions.value = FirebaseRepository.getAllMissions()
            _items.value = FirebaseRepository.getAllItems()
            _characters.value = FirebaseRepository.getAllCharacters()
            _users.value = FirebaseRepository.getAllUsers()
        }
    }

    fun setGames(vararg gameId: String) {
        progressIndicatorListener?.isVisible()
        viewModelScope.launch {
            _games.value = FirebaseRepository.getGame(*gameId)
            progressIndicatorListener?.isGone()
        }
    }

    fun newGame(name: String){
        progressIndicatorListener?.isVisible()
        if (!EditTextError.validateText(name)) {
            progressIndicatorListener?.isGone()
            return
        }

    }
}