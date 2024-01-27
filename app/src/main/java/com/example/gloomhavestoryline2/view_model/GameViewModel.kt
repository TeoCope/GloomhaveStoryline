package com.example.gloomhavestoryline2.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gloomhavestoryline2.db.entities.Game
import com.example.gloomhavestoryline2.db.entities.Mission
import com.example.gloomhavestoryline2.db.repository.FirebaseRepository
import com.example.gloomhavestoryline2.other.enum_class.RequestStatus
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class GameViewModel: ViewModel() {

    private val TAG = "GAME_VIEW_MODEL"

    private val _requestStatus: MutableLiveData<RequestStatus> = MutableLiveData()
    val requestStatus: LiveData<RequestStatus>
        get() = _requestStatus

    private val _game: MutableLiveData<Game> = MutableLiveData()
    val game: LiveData<Game>
        get() = _game


    fun setGame(gameID: String) {
        viewModelScope.launch {
            val result = FirebaseRepository.getGame(gameID) ?: return@launch
            result.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Something went wrong", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val game = snapshot.toObject(Game::class.java)
                    game?.let { _game.value = it}
                }
            }
        }
    }

    fun missionCompleted(){
        Log.d(TAG, "Game: ${game.value}")
        val currentMission = game.value?.currentMission
        val gameId = game.value?.id
        viewModelScope.launch {
            FirebaseRepository.updateGameMission(currentMission, gameId!!)
        }
    }
}