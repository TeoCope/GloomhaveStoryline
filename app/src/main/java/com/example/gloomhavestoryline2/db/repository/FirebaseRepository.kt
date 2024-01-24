package com.example.gloomhavestoryline2.db.repository

import android.util.Log
import com.example.gloomhavestoryline2.db.entities.Ability
import com.example.gloomhavestoryline2.db.entities.Character
import com.example.gloomhavestoryline2.db.entities.Game
import com.example.gloomhavestoryline2.db.entities.Mission
import com.example.gloomhavestoryline2.db.entities.Item
import com.example.gloomhavestoryline2.db.entities.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

object FirebaseRepository {
    val TAG = "FIREBASE_REPOSITORY"
    var firestoreDB = FirebaseFirestore.getInstance()

    suspend fun getAllMissions(): List<Mission> {
        return try {
            firestoreDB.collection("missions").orderBy("number", Query.Direction.ASCENDING).get().await().documents.mapNotNull { it.toObject(Mission::class.java) }
        } catch (e: Exception) {
            Log.e(TAG,"Missions download failure",e)
            emptyList()
        }
    }

    suspend fun getAllItems(): List<Item> {
        return try {
            firestoreDB.collection("items").orderBy("number",Query.Direction.ASCENDING).get().await().documents.mapNotNull { it.toObject(Item::class.java) }
        } catch (e: Exception) {
            Log.e(TAG, "Items download failure", e)
            emptyList()
        }
    }

    suspend fun getAllCharacters(): List<Character> {
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
    }

    suspend fun getAllUsers(): List<User> {
        return try {
            firestoreDB.collection("users").get().await().documents.mapNotNull { it.toObject(User::class.java) }
        } catch (e: Exception) {
            Log.e(TAG, "Users download failure", e)
            emptyList()
        }
    }

    suspend fun getGame(vararg gameID: String): List<Game> {
        return try {
            val games: MutableList<Game> = mutableListOf()
            for (id in gameID) {
                val game = firestoreDB.collection("games").document(id).get().await().toObject(Game::class.java)
                game?.let { games.add(it) }
            }
            return games
        } catch (e: Exception) {
            Log.e(TAG, "Games download failure", e)
            emptyList()
        }
    }

    suspend fun newGame(squadName: String){
        try {
            /*val game = Game(squadName = squadName)
            firestoreDB.collection("games").document().set(game).await()*/
        } catch (e: Exception) {

        }
    }
}