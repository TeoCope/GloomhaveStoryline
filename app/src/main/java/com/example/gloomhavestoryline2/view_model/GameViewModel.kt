package com.example.gloomhavestoryline2.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gloomhavestoryline2.db.entities.Game
import com.example.gloomhavestoryline2.db.repository.FirebaseRepository
import com.example.gloomhavestoryline2.other.enum_class.RequestStatus
import kotlinx.coroutines.launch

class GameViewModel: ViewModel() {

    private val TAG = "GAME_VIEW_MODEL"

    private val _requestStatus: MutableLiveData<RequestStatus> = MutableLiveData()
    val requestStatus: LiveData<RequestStatus>
        get() = _requestStatus

    private val _game: MutableLiveData<Game> = MutableLiveData()
    val game: LiveData<Game>
        get() = _game

    fun setGame(gameId: String) {
        _requestStatus.value = RequestStatus.LOADING
        viewModelScope.launch {
            val result = FirebaseRepository.getGame(gameId)
            if (result == null) {
                _requestStatus.value = RequestStatus.ERROR
                return@launch
            }
            _requestStatus.value = RequestStatus.DONE
            _game.value = result!!
        }
    }
}