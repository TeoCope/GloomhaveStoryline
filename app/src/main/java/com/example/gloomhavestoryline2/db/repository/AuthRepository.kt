package com.example.gloomhavestoryline2.db.repository

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val TAG = "AUTH_REPOSITORY"

    private val auth = FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String): Boolean {
           return try {
               auth.signInWithEmailAndPassword(email,password).await()
               true
           } catch (e: Exception) {
               Log.e(TAG, "Login error", e)
               false
           }
    }

    suspend fun singup(nickname: String, email: String, password: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            FirebaseRepository.newUser(result.user?.uid!!,nickname,email)
        } catch (e: Exception) {
            Log.e(TAG, "Sing up error", e)
            false
        }
    }

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
    }

    suspend fun deleteUser() {
        try {
            val auth = FirebaseAuth.getInstance()
            auth.currentUser?.delete()?.await()
            auth.signOut()
            Log.d(TAG, "Usere delete")
        } catch (e: Exception){
            Log.e(TAG, "Error to delete user: ${auth.currentUser}")
        }
    }
}