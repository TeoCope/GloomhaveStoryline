package com.example.gloomhavestoryline2.db.repository

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val TAG = "AUTH_REPOSITORY"

    private val auth = FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String): AuthResult? {
           return try {
               auth.signInWithEmailAndPassword(email,password).await()
           } catch (e: Exception) {
               Log.e(TAG, "Login error", e)
               null
           }
    }
}