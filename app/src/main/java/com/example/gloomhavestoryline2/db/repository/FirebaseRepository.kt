package com.example.gloomhavestoryline2.db.repository

import android.util.Log
import com.example.gloomhavestoryline2.db.entities.Character
import com.example.gloomhavestoryline2.db.entities.Game
import com.example.gloomhavestoryline2.db.entities.Mission
import com.example.gloomhavestoryline2.db.entities.Item
import com.example.gloomhavestoryline2.db.entities.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

object FirebaseRepository {
    val TAG = "FIREBASE_REPOSITORY"

    var firestoreDB = FirebaseFirestore.getInstance()

    suspend fun getMissions(): List<Mission> {
        return try {
            firestoreDB.collection("missions").orderBy("number", Query.Direction.ASCENDING).get().await().documents.mapNotNull { it.toObject(Mission::class.java) }
        } catch (e: Exception) {
            Log.e(TAG, "Mission dowload failure", e)
            emptyList()
        }
    }

    suspend fun getAllItems(): List<Item> {
        return try {
            firestoreDB.collection("items").orderBy("number", Query.Direction.ASCENDING).get()
                .await().documents.mapNotNull { it.toObject(Item::class.java) }
        } catch (e: Exception) {
            Log.e(TAG, "Items download failure", e)
            emptyList()
        }
    }

    /*suspend fun getAllCharacters(): List<Character> {
        return try {
            firestoreDB.collection("characters").get().await().documents.mapNotNull {
                val name = it.data?.get("name").toString()
                val abilities = firestoreDB.collection("characters").document(name.lowercase()).collection("abilities").get().await().documents.mapNotNull { it.toObject(Ability::class.java) }
                Character(name = name, abilities =  abilities)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Characters download failure", e)
            emptyList()
        }
    }*/

    /* User Collection */
    fun getUser(id: String): DocumentReference? {
        return try {
            //firestoreDB.collection("users").document(id).get().await().toObject(User::class.java)
            firestoreDB.collection("users").document(id)
        } catch (e: Exception) {
            Log.e(TAG, "Users download failure", e)
            null
        }
    }

    suspend fun newUser(id: String, name: String, email: String): Boolean {
        return try {
            val newUser = User(id = id, nickname = name, email = email)
            firestoreDB.collection("users").document(id).set(newUser).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error to add user in database", e)
            false
        }
    }

    suspend fun deleteUser(id: String): Boolean {
        return try {
            firestoreDB.collection("users").document(id).delete().await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error to delete user: $id")
            false
        }
    }

    private suspend fun updateUserGameList(user: User, newGameId: String): Boolean {
        return try {
            firestoreDB.collection("users").document(user.id)
                .update("games", FieldValue.arrayUnion(newGameId)).await()
            firestoreDB.collection("users").document(user.id)
                .update("matchInProgress", user.matchInProgress + 1).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error to update user game list", e)
            false
        }
    }

    /* Games collection */
    fun getGame(gameID: String): DocumentReference?{
        return try {
            firestoreDB.collection("games").document(gameID)
        } catch (e: Exception) {
            Log.e(TAG, "Game download failure", e)
            null
        }
    }

    suspend fun getGames(gameID: List<String>): List<Game> {
        return try {
            val games: MutableList<Game> = mutableListOf()
            for (id in gameID) {
                val game = firestoreDB.collection("games").document(id).get().await()
                    .toObject(Game::class.java)
                game?.let { games.add(it) }
            }
            return games
        } catch (e: Exception) {
            Log.e(TAG, "Games download failure", e)
            emptyList()
        }
    }

    suspend fun newGame(
        squadName: String,
        character: String,
        user: User
    ): String? {
        return try {
            val newCharacter = newSquadMember(user, character) ?: return null
            newCharacter.isHost = true
            val newSquad: MutableList<Character> = mutableListOf()
            newSquad.add(newCharacter)
            val gameId = firestoreDB.collection("games").document().id
            if (newSquad.isNotEmpty()) {
                val newGame = Game(id = gameId, squadName = squadName, squad = newSquad, items = getAllItems(), missions = getMissions())
                newGame.charactersAvailable.remove(character)
                firestoreDB.collection("games").document(gameId).set(newGame)
            }
            if (updateUserGameList(user, gameId)) {
                return gameId
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error to create a new game", e)
            null
        }
    }

    suspend fun updateGameSquad(gameID: String, user: User, character: String): Boolean {
        return try {
            val newCharacter = newSquadMember(user,character)
            firestoreDB.collection("games").document(gameID).update("squad", FieldValue.arrayUnion(newCharacter)).await()
            firestoreDB.collection("games").document(gameID).update("charactersAvailable", FieldValue.arrayRemove(character)).await()
            updateUserGameList(user,gameID)
        } catch (e: Exception) {
            Log.e(TAG, "Error to upload game", e)
            false
        }
    }

    suspend fun updateGameMission(currentMission: Int?,gameID: String){
        try {
            firestoreDB.collection("games").document(gameID).update("currentMission", FieldValue.increment(1)).await()
        } catch (e: Exception) {
            Log.e(TAG, "Game mission update failure", e)
        }
    }

    private suspend fun newSquadMember(user: User, character: String): Character? {
        val newCharacter = getCharacter(character)
        if (newCharacter != null) {
            newCharacter.id = user.id
            newCharacter.nickname = user.nickname
            newCharacter.name = character
        }
        return newCharacter
    }

    private suspend fun getCharacter(character: String): Character? {
        return try {
            var correction: String = character
            if (correction == "Red Guard") {
                correction = "red_guard"
            }
            firestoreDB.collection("characters").document(correction.lowercase()).get().await()
                .toObject(Character::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Character don't found", e)
            null
        }
    }
}