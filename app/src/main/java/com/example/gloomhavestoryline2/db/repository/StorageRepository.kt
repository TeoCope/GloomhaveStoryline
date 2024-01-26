package com.example.gloomhavestoryline2.db.repository

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File

object StorageRepository {
    private val TAG = "STORAGE_REPOSITORY"
    private val storage = Firebase.storage

    suspend fun downloadRule(){
        try {
            Log.d(TAG, "Inizio dowload")
            val ruleReference = storage.reference.child("gloomhaven-rulebook.pdf")
            val localFile = File.createTempFile("application", "pdf")
            ruleReference.getFile(localFile).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error to dowload rulebook", e)
        }
    }
}