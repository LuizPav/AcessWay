package com.example.accessway.repository

import android.util.Log
import com.example.accessway.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val TAG = "UserRepository"

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun saveUser(user: UserProfile): Result<Unit> {
        Log.d(TAG, "saveUser called for uid=${user.uid}")
        return try {
            if (user.uid.isEmpty()) throw Exception("UID do usuário vazio")

            firestore.collection("users")
                .document(user.uid)
                .set(user)
                .await()
            Log.d(TAG, "Successfully saved user profile for uid=${user.uid}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving user profile for uid=${user.uid}", e)
            Result.failure(e)
        }
    }

    suspend fun getUser(uid: String): Result<UserProfile> {
        Log.d(TAG, "getUser called for uid=$uid")
        return try {
            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            val user = document.toObject(UserProfile::class.java)?.copy(uid = document.id)
                ?: throw Exception("Usuário não encontrado")

            Log.d(TAG, "Successfully loaded user profile: name=${user.name}")
            Result.success(user)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading user profile for uid=$uid", e)
            Result.failure(e)
        }
    }
}