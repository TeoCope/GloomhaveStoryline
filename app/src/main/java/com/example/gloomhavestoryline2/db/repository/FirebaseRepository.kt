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
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirebaseRepository {
    val TAG = "FIREBASE_REPOSITORY"

    var firestoreDB = FirebaseFirestore.getInstance()

    suspend fun getAllMissions(): List<Mission> {
        return try {
            firestoreDB.collection("missions").orderBy("number", Query.Direction.ASCENDING).get()
                .await().documents.mapNotNull { it.toObject(Mission::class.java) }
        } catch (e: Exception) {
            Log.e(TAG, "Missions download failure", e)
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
    suspend fun getGame(gameID: String): Game?{
        return try {
            firestoreDB.collection("games").document(gameID).get().await().toObject(Game::class.java)
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
            val newCharacter = getCharacter(character)
            val newSquad: MutableList<Character> = mutableListOf()
            if (newCharacter != null) {
                newCharacter.id = user.id
                newCharacter.nickname = user.nickname
                newCharacter.isHost = true
                newCharacter.name = character
                newSquad.add(newCharacter)
            }
            val gameId = firestoreDB.collection("games").document().id
            if (!newSquad.isEmpty()) {
                val newGame = Game(id = gameId, squadName = squadName, squad = newSquad)
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