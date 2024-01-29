package com.example.gloomhavestoryline2.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gloomhavestoryline2.db.entities.Character
import com.example.gloomhavestoryline2.db.entities.Game
import com.example.gloomhavestoryline2.db.entities.Item
import com.example.gloomhavestoryline2.db.entities.Mission
import com.example.gloomhavestoryline2.db.repository.FirebaseRepository
import com.example.gloomhavestoryline2.other.enum_class.RequestStatus
import com.example.gloomhavestoryline2.other.listeners.ToastMessage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val TAG = "GAME_VIEW_MODEL"
    var toastMessage: ToastMessage? = null

    private val _characterMain: MutableLiveData<Character> = MutableLiveData()
    val characterMain: LiveData<Character>
        get() = _characterMain

    private val _game: MutableLiveData<Game> = MutableLiveData()
    val game: LiveData<Game>
        get() = _game
    private val _squad: MutableLiveData<List<Character>> = MutableLiveData()
    val squad: LiveData<List<Character>>
        get() = _squad
    private val _missions: MutableLiveData<List<Mission>> = MutableLiveData()
    val missions: LiveData<List<Mission>>
        get() = _missions
    private val _items: MutableLiveData<List<Item>> = MutableLiveData()
    val items: LiveData<List<Item>>
        get() = _items


    fun setGame(gameID: String) {
        val result = FirebaseRepository.getGame(gameID) ?: return
        result.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Something went wrong", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val game = snapshot.toObject(Game::class.java)
                game?.let { _game.value = it }
                Log.d(TAG, "Game: $game")
            }
        }
        setCharacterMain()
        setSquad(gameID)
        setMissions(gameID)
        setItems(gameID)
    }

    private fun setSquad(gameID: String) {
        val result = FirebaseRepository.getSquad(gameID) ?: return
        result.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Something went wrong", error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val newSquad = snapshot.documents.mapNotNull { it.toObject(Character::class.java) }
                _squad.value = newSquad
            }
        }
    }

    fun setCharacterMain(){
        val userID = Firebase.auth.uid
        val characterMain = squad.value?.first { it.id == userID }
        _characterMain.value = characterMain ?: return
    }

    private fun setMissions(gameID: String) {
        val result = FirebaseRepository.getMissions(gameID) ?: return
        result.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Something went wrong", error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val missionList = snapshot.documents.mapNotNull { it.toObject(Mission::class.java) }
                _missions.value = missionList
                Log.d(TAG, "Missions: ${missions.value}")
            }
        }
    }

    private fun setItems(gameID: String) {
        val result = FirebaseRepository.getItems(gameID) ?: return
        result.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Something went wrong", error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val itemsList = snapshot.documents.mapNotNull { it.toObject(Item::class.java) }
                _items.value = itemsList
                Log.d(TAG, "Items: ${items.value}")
            }
        }
    }

    fun missionCompleted(missionName: String) {
        val gameId = game.value?.id
        viewModelScope.launch {
            FirebaseRepository.updateGameMission(gameId!!, missionName)
        }
    }

    fun gameCompleted(){
        val userID = characterMain.value?.id
        toastMessage?.showToast("Game completed")
        viewModelScope.launch {
            FirebaseRepository.gameCompleted(userID)
        }
    }

    fun buyItem(item: Item) {
        val userID = characterMain.value?.id
        val gameID = game.value?.id
        if (characterMain.value?.money!! < item.cost){
            toastMessage?.showToast("You do not have enough money")
            return
        }
        viewModelScope.launch {
            FirebaseRepository.buyItem(item,userID,gameID)
        }
    }

    fun assignUser(import: String, value: String) {
        val money = import.toDouble()
        var experience = value.toDouble()
        var levelUp = false
        if ((experience + characterMain.value?.experience!! >= 100)) {
            experience -= 100.00
            levelUp = true
        }
        if (characterMain.value?.level!! == 10) {
            experience = 0.00
            levelUp = false
        }
        val userID = characterMain.value?.id
        val gameID = game.value?.id
        viewModelScope.launch {
            FirebaseRepository.updateUser(money, experience, userID,gameID)
            if (levelUp) {
                FirebaseRepository.levelUp(gameID,userID)
            }
        }
    }

    fun sellItem(item: Item) {
        val gameID = game.value?.id
        val userID = characterMain.value?.id
        viewModelScope.launch {
            FirebaseRepository.sellItem(gameID, item, userID)
        }
    }

    fun deleteGame() {
        val gameID = game.value?.id
        val squad = squad.value
        viewModelScope.launch {
            FirebaseRepository.setGameEnd(gameID)
            FirebaseRepository.deleteGame(gameID,squad)
        }
    }
}