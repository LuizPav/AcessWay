package com.example.accessway.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

private const val TAG = "AuthRepository"

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    suspend fun register(
        email: String,
        password: String
    ): Result<String> {
        Log.d(TAG, "register called for email=$email")
        return try {
            val result = auth
                .createUserWithEmailAndPassword(email, password)
                .await()
            val uid = result.user!!.uid
            Log.d(TAG, "Registration successful for email=$email, uid=$uid")
            Result.success(uid)
        } catch (e: Exception) {
            Log.e(TAG, "Registration failed for email=$email", e)
            Result.failure(e)
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): Result<String> {
        Log.d(TAG, "login called for email=$email")
        return try {
            val result = auth
                .signInWithEmailAndPassword(email, password)
                .await()
            val uid = result.user!!.uid
            Log.d(TAG, "Login successful for email=$email, uid=$uid")
            Result.success(uid)
        } catch (e: Exception) {
            Log.e(TAG, "Login failed for email=$email", e)
            Result.failure(e)
        }
    }

    fun logout() {
        Log.d(TAG, "logout called for uid=${getCurrentUserUid()}")
        auth.signOut()
    }

    fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }
}