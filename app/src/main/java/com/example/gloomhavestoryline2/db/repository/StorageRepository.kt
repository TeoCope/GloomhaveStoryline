package com.example.gloomhavestoryline2.db.repository

import android.net.Uri
import android.os.Environment
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File

object StorageRepository {
    private val TAG = "STORAGE_REPOSITORY"
    private val storage = Firebase.storage

    suspend fun downloadRule() {
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        try {
            val ruleReference = storage.reference.child("gloomhaven-rulebook.pdf")
            val localFile = File(downloadDir, ruleReference.name)
            ruleReference.getFile(localFile).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error to dowload rulebook", e)
        }
    }

    fun downloadUserImage(userId: String): StorageReference {
        return storage.reference.child("users_image/$userId")
    }

    fun downloadItemImage(nameItem: String): StorageReference {
        return storage.reference.child("items/$nameItem")
    }

    fun downloadAbilityImage(image: String): StorageReference {
        return storage.reference.child("abilities/$image")
    }
}